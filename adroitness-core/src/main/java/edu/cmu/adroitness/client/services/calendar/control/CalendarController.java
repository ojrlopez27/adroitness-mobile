package edu.cmu.adroitness.client.services.calendar.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;
import edu.cmu.adroitness.client.services.calendar.view.CalendarActivity;
import edu.cmu.adroitness.client.services.calendar.view.CalendarDayDetailActivity;
import edu.cmu.adroitness.client.services.calendar.view.EventCreationFragment;


/**
 * Created by oscarr on 1/3/15.
 */
public class CalendarController {
    protected EventCreationFragment eventCreationFragment;
    protected static CalendarController instance;
    protected MessageBroker mMB;
    protected Activity mActivity;
    protected String accountName;

    protected CalendarController(Activity activity) {
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static CalendarController getInstance(Activity activity) {
        if (instance == null) {
            instance = new CalendarController(activity);
        }
        return instance;
    }

    public static CalendarController getInstance() {
        return getInstance(null);
    }

    public void setEventCreationFragment(EventCreationFragment eventCreationFragment) {
        this.eventCreationFragment = eventCreationFragment;
    }

    // ****************************** CALENDAR ***********************************************


    /**
     * We get a local copy of the calendar event from the service
     * @param forSpecificDate if null, it will bring all calendar events, otherwise only the events
     *                for the selected date
     * @param numberOfMonths
     */
    public void getCalendarEvents(Object subscriber, Date forSpecificDate, int numberOfMonths) {
        boolean execute = false;
        if( subscriber instanceof CalendarActivity ){
            if( !((CalendarActivity)subscriber).isQueryExecuted() ) {
                ((CalendarActivity) subscriber).setQueryExecuted(true);
                execute = true;
            }
        }else if( subscriber instanceof CalendarDayDetailActivity ){
            if( !((CalendarDayDetailActivity)subscriber).isQueryExecuted() ) {
                ((CalendarDayDetailActivity) subscriber).setQueryExecuted(true);
                execute = true;
            }
        }
        if( execute ) {
            mMB.sendAndReceive(subscriber, CalendarNotificationEvent.class,
                    MBRequest.build(Constants.MSG_GET_CALENDAR_EVENTS)
                            .put(Constants.CALENDAR_FOR_SPECIFIC_DATE, forSpecificDate)
                            .put(Constants.CALENDAR_NUMBER_OF_MONTHS, numberOfMonths));
        }
    }

    public void createCalendarEvent(Object caller) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_INSERT_EVENT)
                        .put(Constants.CALENDAR_EVENT_DATA, createEventVO()));
    }

    public void updateCalendarEvent(Object caller, String eventId) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_UPDATE_EVENT)
                        .put(Constants.CALENDAR_EVENT_DATA, createEventVO().setId(eventId)));
    }

    public void deleteEvents(Object caller, List<CalendarEventVO> deleteEventsList, List<CalendarEventVO> events) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_DELETE_EVENTS)
                        .put(Constants.CALENDAR_EVENT_DATA, deleteEventsList));
        for( CalendarEventVO eventToDelete : deleteEventsList ){
            for( CalendarEventVO originalEvent : events){
                if( eventToDelete.getId().equals( originalEvent.getId() )){
                    events.remove( originalEvent );
                    break;
                }
            }
        }
    }

    public void deleteAllEvents(Object caller) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_DELETE_ALL_EVENTS)
                        .put(Constants.CALENDAR_EVENT_DATA, null));
    }


    protected CalendarEventVO createEventVO( ){
        CalendarEventVO event = new CalendarEventVO()
                .setSummary( eventCreationFragment.getSummary().getText().toString() )
                .setLocation( eventCreationFragment.getLocation().getText().toString() )
                .setDescription( eventCreationFragment.getDescription().getText().toString() );

        event.setStartDate( Util.getDateTime((Date) eventCreationFragment.getStartDate().getTag(),
                eventCreationFragment.getStartTime().getText().toString()));
        event.setEndDate( Util.getDateTime((Date) eventCreationFragment.getEndDate().getTag(),
                eventCreationFragment.getEndTime().getText().toString()));

        ArrayList<String> attendees = new ArrayList<>();
        attendees.add("somebody@cs.cmu.edu");
        event.setAttendees( attendees );
        event.setEmailReminder(24 * 60); // one day in advance
        event.setSmsReminder(10); // 10 minutes in advance
        event.setNumberOfMonths(0);
        return event;
    }

    public void setSyncTime(long syncTime) {
        mMB.send(mActivity,
                MBRequest.build(Constants.MSG_SET_CALENDAR_SYNC_TIME)
                        .put(Constants.CALENDAR_SYNC_TIME, syncTime) );
    }


    // ****************************** GOOGLE ***********************************************

    public void chooseAccount(Activity mActivity) {
        mMB.send(mActivity,
                MBRequest.build(Constants.MSG_CHOOSE_ACCOUNT)
                .put(Constants.BUNDLE_ACTIVITY_NAME, mActivity));
    }

    public void checkGooglePlayServicesAvailable() {
        mMB.send(mActivity,
                MBRequest.build(Constants.MSG_GOOGLE_PLAY_AVAILABLE));
    }

    public void showGooglePlayServicesAvailabilityErrorDialog(Integer connStatus, Activity activity){
        mMB.send(activity,
                MBRequest.build(Constants.MSG_GOOGLE_PLAY_AVAILABLE_ERROR)
                .put(Constants.GOOGLE_CONNECTION_STATUS, connStatus)
                .put(Constants.APP_CONTEXT, activity));
    }

    public void configAccountName(Activity activity, String accountName) {
        mMB.send(activity,
                MBRequest.build(Constants.MSG_CONFIG_ACCOUNT_NAME)
                .put(Constants.BUNDLE_ACTIVITY_NAME, activity)
                .put(Constants.ACCOUNT_NAME, accountName)
                .put(Constants.ACCOUNT_TYPE, Constants.GOOGLE_ACCOUNT));
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
}
