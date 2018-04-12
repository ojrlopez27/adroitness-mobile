package edu.cmu.adroitness.client.services.calendar.control;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.commons.view.AccountPickerActivity;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;
import edu.cmu.adroitness.client.services.generic.control.GenericService;

public class CalendarService extends GenericService {
    /**
     * A Google Calendar API service object used to access the API.
     * Note: Do not confuse this class with API library's model classes, which
     * represent specific data structures.
     */
    private Calendar calendar;
    private TreeMap<String, CalendarEventVO> events;
    private HttpTransport transport = AndroidHttp.newCompatibleTransport();
    private JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private static final String CALENDAR_ID = "primary";
    private String syncToken = null;
    private String pageToken = null; // Retrieve the events, one page at a time.
    private Map<String, String> syncSettingsDataStore;
    private String SYNC_TOKEN_KEY = "SYNC_TOKEN_KEY";
    private Boolean firstLoad = true;
    private Integer defaultNumOfMonths = 12;
    private Integer defaultMbRequestId = -1;

    /**
     * update calendar automatically
     **/
    private Timer timer;
    private static Long mUpdateTime = 2 * 60 * 1000L; // 2 minmUpdateTime
    private List<CalendarEventVO> processedEvents;

    public CalendarService(){
        super(null);
        if( actions.isEmpty() ) {
            this.actions.add(Constants.ACTION_CALENDAR);
        }
        syncSettingsDataStore = new HashMap<>();
        events = new TreeMap<>();
        processedEvents = new ArrayList<>();
    }

    @Override
    public void doAfterBind(){
        super.doAfterBind();
        checkInitialization();
    }

    public synchronized void initializeCalendar(){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if( UtilServiceAPIs.credential == null && !UtilServiceAPIs.isAccountSelected) {
                    UtilServiceAPIs.initializeCredentials(mContext, null);
                    calendar = new com.google.api.services.calendar.Calendar.Builder(
                            transport, jsonFactory, UtilServiceAPIs.credential)
                            .setApplicationName("Adroitness")
                            .build();
                    if( resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null ) {
                        processEvents(Constants.CALENDAR_GET_EVENTS, (List)null, defaultMbRequestId);
                    }
                }
                if(UtilServiceAPIs.credential != null && UtilServiceAPIs.isAccountSelected){
                    calendar = new com.google.api.services.calendar.Calendar.Builder(
                            transport, jsonFactory, UtilServiceAPIs.credential)
                            .setApplicationName("Adroitness")
                            .build();
                    if( resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null ) {
                        processEvents(Constants.CALENDAR_GET_EVENTS, (List)null, defaultMbRequestId);
                    }
                }

            }
        });
    }

    /********  CRUD OPERATIONS   ******************************************************************/

    /**
     * Creates a new calendar event
     * @param event
     * @throws IOException
     */
    private synchronized CalendarEventVO createEvent(final CalendarEventVO event,
                                                     final Integer mbRequestId ) throws IOException {
        return Util.executeSync(new Callable<CalendarEventVO>() {
            @Override
            public CalendarEventVO call() throws Exception {
                if (event != null) {
                    Event eventData = convertToEventData(event);
                    if (checkDates(event, mbRequestId)) {
                        return convertToEventVO( calendar.events().insert(CALENDAR_ID, eventData).execute() );
                    }
                } else {
                    mb.send(CalendarService.this,
                            CalendarNotificationEvent.build()
                                    .setNotification("Calendar Event is null")
                                    .setIsError(true)
                                    .setMbRequestId(mbRequestId));
                }
                return null;
            }
        });
    }

    /**
     * Queries all calendar events from fromDate to a certain amount of months ahead
     * @param fromDate
     * @param numberOfMonths
     * @return
     * @throws IOException
     */
    private synchronized TreeMap<String, CalendarEventVO> getDataFromApi(Date fromDate, Integer numberOfMonths,
                                                               Boolean sendNotification,
                                                               Integer mbRequestId) throws IOException {
        DateTime from = new DateTime( fromDate == null? new Date(System.currentTimeMillis()) : fromDate );
        return getDataFromApi(from, numberOfMonths, sendNotification, mbRequestId);
    }

    public List<CalendarEventVO> getEvents() {
        if (events != null){
            return new ArrayList<>(events.values());
        }
        return null;
    }

    /**
     * Queries all calendar events from fromDate to a certain amount of months ahead
     * @param fromDate
     * @param numberOfMonths
     * @return
     * @throws IOException
     */
    private synchronized TreeMap<String, CalendarEventVO> getDataFromApi(final DateTime fromDate,
                                                               final Integer numberOfMonths,
                                                               final Boolean sendNotification,
                                                               final Integer mbRequestId){
        return Util.executeSync(new Callable<TreeMap<String, CalendarEventVO>>() {
            @Override
            public TreeMap<String, CalendarEventVO> call() throws Exception {

                if( numberOfMonths == 0 && (!events.values().isEmpty() || sendNotification) ){
                    sendNotification(Constants.CALENDAR_AFTER_QUERY_EVENTS, filterEvents(
                            new Date( fromDate.getValue())), mbRequestId);
                }else {
                    // Construct the {@link Calendar.Events.List} request, but don't execute it yet.
                    Calendar.Events.List request = null;
                    try {
                        request = calendar.events().list(CALENDAR_ID);
                    }catch(UserRecoverableAuthIOException userRecoverableAuthIOException)
                    {
                        mb.send(CalendarService.this,
                                CalendarNotificationEvent.build()
                                        .setNotification(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)
                                        .setParams(userRecoverableAuthIOException.getIntent()));
                    }
                    DateTime toDate = new DateTime(Util.getRelativeDate(java.util.Calendar.MONTH,
                            numberOfMonths));

                    // Load the sync token stored from the last execution, if any.
                    syncToken = syncSettingsDataStore.get(SYNC_TOKEN_KEY);
                    if (syncToken == null) {
                        // performing full sync
                        request.setTimeMin(fromDate).setTimeMax(toDate);
                        firstLoad = true;
                    } else {
                        // Performing incremental sync
                        request.setSyncToken(syncToken);
                        firstLoad = false;
                    }
                    searchEvents(request, fromDate, numberOfMonths, sendNotification, mbRequestId);
                }
                return events;
            }
        });
    }


    /**
     * Retrieve the events, one page at a time.
     * @param request
     * @param fromDate
     * @param numberOfMonths
     * @return
     * @throws Exception
     */
    private void searchEvents(Calendar.Events.List request, DateTime fromDate, Integer numberOfMonths,
                              Boolean sendNotification, Integer mbRequestId) throws Exception{
        Events eventsQuery = null;
        List<Event> items = new ArrayList<>();
        do {
            request.setPageToken(pageToken);
            try {
                eventsQuery = request.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 410) {
                    // A 410 status code, "Gone", indicates that the sync token is invalid.
                    System.out.println("Invalid sync token, clearing event store and re-syncing.");
                    syncSettingsDataStore.remove(SYNC_TOKEN_KEY);
                    eventsQuery.clear();
                    getDataFromApi(fromDate, numberOfMonths, sendNotification, mbRequestId);
                } else {
                    throw e;
                }
            }
            catch (UserRecoverableAuthIOException userRecoverableException) {
                userRecoverableException.printStackTrace();
                mb.send(CalendarService.this,
                        CalendarNotificationEvent.build()
                                .setNotification(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)
                                .setParams(userRecoverableException.getIntent()));
            }

            catch (Exception e) {
                e.printStackTrace();

            }

            if(eventsQuery!=null) {
                items.addAll(eventsQuery.getItems());
                pageToken = eventsQuery.getNextPageToken();
            }

        } while (pageToken != null);

        if ( !items.isEmpty() ) {
            createHashMapEvents( items, sendNotification, mbRequestId );
        }

        if(eventsQuery!=null) {
            // Store the sync token from the last request to be used during the next execution.
            syncSettingsDataStore.put(SYNC_TOKEN_KEY, eventsQuery.getNextSyncToken());
        }
    }

    private ArrayList<CalendarEventVO> filterEvents(Date selectedDate) {
        ArrayList<CalendarEventVO> selectedEvents = new ArrayList<>();
        if( selectedDate != null ) {
            if (events != null && !events.isEmpty()) {
                for (CalendarEventVO calEvent : events.values()) {
                    if (Util.getOnlyeDate(calEvent.getStartDate()).compareTo(selectedDate) == 0) {
                        selectedEvents.add(calEvent);
                    }
                }
            }
        }
        return selectedEvents;
    }


    private synchronized void createHashMapEvents( List<Event> listEvents, Boolean sendNotification,
                                                   Integer mbRequestId){
        List<CalendarEventVO> updatedEvents = new ArrayList<>();
        List<CalendarEventVO> newEvents = new ArrayList<>();
        List<CalendarEventVO> deletedEvents = new ArrayList<>();

        boolean shouldSend = false;
        for(Event event : listEvents){
            CalendarEventVO eventVO = convertToEventVO( event );
            if( !event.getStatus().equals("cancelled") && !checkAlreadySentNotification( eventVO ) ){
                shouldSend = true;
            }
            if( event.getStatus().equals("cancelled") ){
                deletedEvents.add( eventVO );
                events.remove( getEventKey(event) );
            }else if( event.getStatus().equals("confirmed") && (firstLoad
                    || ( event.getUpdated().getValue() - event.getCreated().getValue() <= 1000) ) ){
                events.put(getEventKey(event), eventVO);
                newEvents.add( eventVO );
            }else{
                events.put(getEventKey(event), eventVO);
                updatedEvents.add( eventVO );
            }
        }

        if( !firstLoad && sendNotification && shouldSend ) {
            if (!newEvents.isEmpty() ) {
                sendNotification(Constants.CALENDAR_AFTER_CREATE_EVENT, newEvents, mbRequestId);
            }
            if (!updatedEvents.isEmpty() ) {
                sendNotification(Constants.CALENDAR_AFTER_UPDATE_EVENT, updatedEvents, mbRequestId);
            }
            if (!deletedEvents.isEmpty() ) {
                sendNotification(Constants.CALENDAR_AFTER_DELETE_EVENTS, deletedEvents, mbRequestId);
            }
        }
    }

    private boolean checkAlreadySentNotification(CalendarEventVO eventVO) {
        for(CalendarEventVO event : processedEvents ){
            if( event.compareTo(eventVO) == 0 ){
                processedEvents.remove(event);
                return true;
            }
        }
        return false;
    }


    private void sendNotification(String message, List<CalendarEventVO> modifiedEvents, Integer mbRequestId){
        mb.send(CalendarService.this,
                CalendarNotificationEvent.build()
                        .setNotification(message)
                        .setAllEvents(new ArrayList<>(events.values()))
                        .setSyncEvents( modifiedEvents )
                        .setMbRequestId(mbRequestId));
    }


    private String getEventKey(Event event) {
        return event.getId();
    }

    private String getEventKey(CalendarEventVO event) {
        return event.getId();
    }


    /**
     * Deletes all calendar events from Jan 1 1900 to 1 year ahead of current date (12 months)
     * @throws Exception
     */
    private void deleteAllEvents(final Integer mbRequestId) throws Exception{
        Util.execute(new Runnable() {
            @Override
            public void run() {
                getDataFromApi( new DateTime("1900-01-01T01:00:00.000-04:00"), defaultNumOfMonths,
                        false, mbRequestId);
                for (CalendarEventVO event : events.values()) {
                    deleteEvent(event.getId());
                }
            }
        });
    }

    /**
     * Updates the specified calendar event
     * @param eventData
     * @throws Exception
     */
    private CalendarEventVO updateEvent(final CalendarEventVO eventData) {
        return Util.executeSync(new Callable<CalendarEventVO>() {
            @Override
            public CalendarEventVO call() throws Exception {
                calendar.events().update(CALENDAR_ID, eventData.getId(),
                        convertToEventData( eventData ) ).execute();
                return eventData;
            }
        });
    }

    /**
     * Deletes a specified calendar event
     * @param eventId
     * @throws Exception
     */
    private void deleteEvent(final String eventId){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    calendar.events().delete(CALENDAR_ID, eventId).execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /********  END CRUD OPERATIONS   **************************************************************/


    private synchronized Event convertToEventData( final CalendarEventVO eventData ){
        return Util.executeSync(new Callable<Event>() {
            @Override
            public Event call() throws Exception {
                Event event = new Event()
                        .setSummary( eventData.getSummary() )
                        .setLocation( eventData.getLocation() )
                        .setDescription( eventData.getDescription() )
                        .setId( eventData.getId() );

                DateTime startDateTime = new DateTime( eventData.getStartDateTime() ); //"2015-08-30T10:00:00-04:00");
                EventDateTime start = new EventDateTime().setDateTime(startDateTime);
                event.setStart(start);

                DateTime endDateTime = new DateTime( eventData.getEndDateTime() ); //"2015-08-30T11:00:00-04:00");
                EventDateTime end = new EventDateTime().setDateTime(endDateTime);
                event.setEnd(end);

                /*** In case we need to set attendees and event recurrence
                event.setRecurrence(Arrays.asList( eventData.getRecurrence() ));
                ArrayList<EventAttendee> attendees = new ArrayList<>();
                for( String attendee : eventData.getAttendees() ){
                        attendees.add( new EventAttendee().setEmail( attendee ) );
                }
                event.setAttendees(attendees); **/

                EventReminder[] reminderOverrides = new EventReminder[] {
                        new EventReminder().setMethod("email").setMinutes( eventData.getEmailReminder() ),
                        new EventReminder().setMethod("sms").setMinutes( eventData.getSmsReminder() ),
                };
                Event.Reminders reminders = new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminderOverrides));
                event.setReminders(reminders);

                return event;
            }
        });
    }


    private synchronized CalendarEventVO convertToEventVO( final Event eventData ){
        return Util.executeSync(new Callable<CalendarEventVO>() {
            @Override
            public CalendarEventVO call() throws Exception {
                return convertToCalendarEventData(eventData);
            }
        });
    }

    private CalendarEventVO convertToCalendarEventData(Event eventData) {
        CalendarEventVO event = new CalendarEventVO()
                .setSummary( eventData.getSummary() )
                .setLocation(eventData.getLocation())
                .setDescription(eventData.getDescription())
                .setId(eventData.getId());
        if( eventData.getStart() != null ) {
            event.setStartDate(new Date(getDateTimeFromEvent(eventData.getStart()).getValue()));
        }
        if( eventData.getEnd() != null ) {
            event.setEndDate(new Date(getDateTimeFromEvent(eventData.getEnd()).getValue()));
        }
        event.setRecurrence( eventData.getRecurrence() );
        if( eventData.getAttendees() != null ) {
            ArrayList<String> attendees = new ArrayList<>();
            for (EventAttendee attendee : eventData.getAttendees()) {
                attendees.add(attendee.getEmail());
            }
            event.setAttendees(attendees);
        }
        if(eventData.getReminders() != null && eventData.getReminders().getOverrides() != null) {
            for(EventReminder reminder : eventData.getReminders().getOverrides() ){
                if(reminder.getMethod().equals("email")){
                    event.setEmailReminder( reminder.getMinutes());
                }else if(reminder.getMethod().equals("sms")){
                    event.setSmsReminder( reminder.getMinutes() );
                }
            }
        }
        if( eventData.getCreated() != null ) {
            event.setCreatedTime(eventData.getCreated().toStringRfc3339());
        }
        return event;
    }

    private List<CalendarEventVO> convertToEventVOs(Collection<Event> events){
        List<CalendarEventVO> eventsVO = new ArrayList<>();
        for(Event event : events ){
            eventsVO.add( convertToCalendarEventData(event) );
        }
        return eventsVO;
    }


    /**
     * This checks whether a specific event is in conflict with another one
     * @param eventData
     * @return
     * @throws IOException
     */
    private synchronized boolean checkDates( final CalendarEventVO eventData, final Integer mbRequestId )
            throws IOException {
        return Util.executeSync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Date newEventStartDate = eventData.getStartDate();
                Date newEventEndDate = eventData.getEndDate();

                for(CalendarEventVO event : events.values()){
                    Date eventStartDate = event.getStartDate();
                    Date eventEndDate = event.getEndDate();
                    if( ( newEventStartDate.compareTo(eventEndDate) >= 0
                            && newEventEndDate.compareTo( eventEndDate ) > 0
                            && newEventStartDate.compareTo( newEventEndDate) < 0 )
                            || ( newEventEndDate.compareTo(eventStartDate) <= 0
                            && newEventStartDate.compareTo( eventEndDate ) < 0
                            && newEventStartDate.compareTo( newEventEndDate) < 0 ) ) {
                        continue;
                    }else{
                        String message = event.getDescription() + "\nSummary: " + event.getSummary()
                                + "\nFrom: " + eventStartDate.toString() + " to "
                                + eventEndDate.toString();
                        mb.send(CalendarService.this,
                                CalendarNotificationEvent.build()
                                        .setNotification("Conflict with the event:\n" + message)
                                        .setIsError(true).setMbRequestId(mbRequestId));
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private synchronized DateTime getDateTimeFromEvent(EventDateTime dateTime){
        if( dateTime.getDateTime() != null ){
            return dateTime.getDateTime();
        }
        if( dateTime.getDate() != null ) {
            return dateTime.getDate();
        }
        return new DateTime( new Date( System.currentTimeMillis() ) );
    }

    private void checkInitialization(){
        if( calendar == null ){
            initializeCalendar();
        }
        if( timer == null ) {
            timer = new Timer();
            timer.schedule(new CalendarTimerTask(), mUpdateTime, mUpdateTime);
        }
    }

    public synchronized void processEvents(final Integer mode, final CalendarEventVO eventData, final Integer requestId) {
        List<CalendarEventVO> list = new ArrayList<>();
        list.add( eventData );
        processEvents(mode, list, requestId);
    }

    /**
     * task to call Google Calendar API.
     */
    public synchronized void processEvents(final Integer mode, final List<CalendarEventVO> eventObjects, final Integer requestId) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInitialization();
                    CalendarEventVO eventData = eventObjects != null && eventObjects.size() == 1 ?
                            eventObjects.get(0) : null;
                    List<CalendarEventVO> eventVOs = eventData != null ? null : eventObjects;
                    Date forDate = eventData != null ? eventData.getFromDate() : null;
                    int numberOfMonths = eventData != null ? eventData.getNumberOfMonths() : defaultNumOfMonths;

                    // we need to make sure if there are events to manipulate
                    // (insert, update, delete...)
                    if (events == null && mode != Constants.CALENDAR_GET_EVENTS) {
                        events = getDataFromApi(forDate, numberOfMonths, false, requestId);
                    }
                    switch (mode) {
                        case Constants.CALENDAR_GET_EVENTS:
                            getAndSendEvents(forDate, numberOfMonths, requestId);
                            break;
                        case Constants.CALENDAR_INSERT_EVENT:
                            insertAndSendEvents(eventData, requestId);
                            break;
                        case Constants.CALENDAR_DELETE_EVENTS:
                            deleteAndSendEvents(eventVOs, requestId);
                            break;
                        case Constants.CALENDAR_UPDATE_EVENT:
                            updateAndSendEvents(eventData, requestId);
                            break;

                        case Constants.CALENDAR_DELETE_ALL_EVENTS:
                            deleteAllAndSendEvents(requestId);
                            break;
                    }
                } catch (GooglePlayServicesAvailabilityIOException ex) {
                    sendException(Constants.CALENDAR_AVAILABILITY_EXCEPTION, ex.getConnectionStatusCode());
                } catch (UserRecoverableAuthIOException ex) {
                    sendException(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION, ex.getIntent());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendException("The following error occurred: " + ex.getMessage(), null);
                }
            }
        });
    }


    private void sendException(String notification, Object params){
        mb.send(CalendarService.this, CalendarNotificationEvent.build()
                .setNotification(notification).setParams(params));
    }

    private void deleteAllAndSendEvents(int requestId) throws Exception{
        deleteAllEvents(requestId);
        processedEvents.addAll( events.values() );
        mb.send(CalendarService.this,
                CalendarNotificationEvent.build()
                        .setNotification(Constants.CALENDAR_AFTER_DELETE_ALL_EVENTS)
                        .setAllEvents( new ArrayList<CalendarEventVO>() )
                        .setDeletedEvents( new ArrayList<>( events.values()) )
                        .setMbRequestId(requestId));
        events.clear();
    }

    private void updateAndSendEvents(CalendarEventVO eventData, int requestId) throws Exception{
        CalendarEventVO resultEvent = updateEvent(eventData);
        if (resultEvent != null) {
            processedEvents.add(resultEvent);
            Date forDate = eventData.getStartDate();
            events.remove(getEventKey(resultEvent));
            events.put(getEventKey(resultEvent), resultEvent);
            mb.send(CalendarService.this,
                    CalendarNotificationEvent.build()
                            .setNotification(Constants.CALENDAR_AFTER_UPDATE_EVENT)
                            .setEvents( filterEvents(Util.getOnlyeDate(forDate)) )
                            .setAllEvents( new ArrayList<>(events.values()))
                            .setUpdatedEvent(resultEvent )
                            .setMbRequestId(requestId));
        } else {
            mb.send(CalendarService.this,
                    CalendarNotificationEvent.build()
                            .setNotification("Calendar Event is null")
                            .setIsError(true));
        }
    }

    private void deleteAndSendEvents(List<CalendarEventVO> eventVOs, int requestId) {
        Date forDate = null;
        if (eventVOs != null) {
            forDate = Util.getOnlyeDate( eventVOs.get(0).getStartDate());
            for (CalendarEventVO eventVO : eventVOs) {
                deleteEvent(eventVO.getId());
                events.remove(getEventKey(eventVO));
                processedEvents.add(eventVO);
            }
        }
        mb.send(CalendarService.this,
                CalendarNotificationEvent.build()
                        .setNotification(Constants.CALENDAR_AFTER_DELETE_EVENTS)
                        .setEvents( filterEvents(Util.getOnlyeDate(forDate)) )
                        .setAllEvents( new ArrayList<>(events.values()))
                        .setDeletedEvents(eventVOs)
                        .setMbRequestId(requestId));
    }

    private void insertAndSendEvents(CalendarEventVO eventData, int requestId)
            throws Exception{
        CalendarEventVO resultEvent = createEvent(eventData, requestId);
        if (resultEvent != null) {
            processedEvents.add(resultEvent);
            Date forDate = eventData.getStartDate();
            events.put(getEventKey(resultEvent), resultEvent);
            mb.send(CalendarService.this,
                    CalendarNotificationEvent.build()
                            .setNotification(Constants.CALENDAR_AFTER_CREATE_EVENT)
                            .setEvents( filterEvents(Util.getOnlyeDate(forDate)) )
                            .setAllEvents( new ArrayList<>(events.values()))
                            .setNewEvent(resultEvent)
                            .setMbRequestId(requestId) );
        }
    }

    private void getAndSendEvents(Date fromDate, int numberOfMonths, int requestId) throws Exception{
        events = getDataFromApi(fromDate, numberOfMonths, false, requestId);
        if( numberOfMonths != 0 ) {
            mb.send(CalendarService.this,
                    CalendarNotificationEvent.build()
                            .setNotification(Constants.CALENDAR_AFTER_QUERY_EVENTS)
                            .setEvents( filterEvents(Util.getOnlyeDate(fromDate)) )
                            .setAllEvents( new ArrayList<>(events.values()))
                            .setMbRequestId(requestId));
        }
    }

    public synchronized void getCalendarEvents(final Date fromDate, final Integer numMonths, final Integer requestId) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                CalendarEventVO eventVO = new CalendarEventVO();
                eventVO.setFromDate( fromDate );
                eventVO.setNumberOfMonths( numMonths );
                if (UtilServiceAPIs.credential != null) {
                    if (UtilServiceAPIs.credential.getSelectedAccountName() == null
                            && !UtilServiceAPIs.chooseAccountInProcess) {
                        UtilServiceAPIs.chooseAccount( null );
                        getCalendarEvents( fromDate, numMonths, requestId );
                    } else {
                        if (UtilServiceAPIs.isAccountSelected) {
                            if (UtilServiceAPIs.isDeviceOnline( null )) {
                                processEvents((Constants.CALENDAR_GET_EVENTS), eventVO, requestId);
                            } else {
                                mb.send(CalendarService.this,
                                        CalendarNotificationEvent.build()
                                                .setNotification("No network connection available.")
                                                .setMbRequestId(requestId) );
                            }
                        } else {
                            Util.sleep(1000);
                            getCalendarEvents(fromDate, numMonths, requestId);
                        }
                    }
                }else{
                    initializeCalendar();
                }
            }
        });
    }

    /**
     * EVENTS HANDLER
     * @param event
     */
    public void onEvent( CalendarNotificationEvent event ){
        if( event.getNotification().equals(Constants.CALENDAR_CHECK_REFRESH_CALENDAR) ){
            getCalendarEvents( null, defaultNumOfMonths, defaultMbRequestId);
        }
    }

    public void setSyncTime(Long syncTime) {
        mUpdateTime = syncTime;
        if( timer != null ){
            timer.cancel();
            timer = new Timer();
            timer.schedule(new CalendarTimerTask(), mUpdateTime, mUpdateTime);
        }
    }

    /**
     * This component checks whether there is an updated list of news articles or not.
     */
    private final class CalendarTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                if( resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null ) {
                    getDataFromApi(new Date(), defaultNumOfMonths, true, null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy(){
        calendar = null;
        events = null;
        transport = null;
        jsonFactory = null;
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MiddlewareNotificationEvent event){
        if( event.getSourceName().equals( AccountPickerActivity.class.getName() ) ){
            initializeCalendar();
        }
    }
}
