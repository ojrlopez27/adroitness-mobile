package edu.cmu.adroitness.client.services.calendar.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.GooglePlayServicesEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.calendar.control.CalendarController;
import edu.cmu.adroitness.client.services.calendar.control.CalendarEventAdapter;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;
import edu.cmu.adroitness.client.services.generic.view.ServicesActivity;

import edu.cmu.adroitness.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This is an example of how you can operate over calendar events:
 * 1. QUERY CALENDAR EVENTS (By selected date)
 * 2. DELETE CALENDAR EVENTS
 */
public class CalendarDayDetailActivity extends ServicesActivity {

    protected CalendarController controller;
    protected EventCreationFragment fragment;
    protected TextView statusText;
    protected static CalendarEventAdapter eventsAdapter;
    protected static ListView listview;
    protected static List<CalendarEventVO> events;
    protected static List<CalendarEventVO> deleteEventsList;
    protected static boolean isModifyEvent = false;
    protected static CalendarEventVO eventToModify;
    protected static boolean isRearrangeElements = false;
    protected static boolean goBack = false;
    protected static String dateClicked;
    protected static SimpleDateFormat simpleDateFormat;
    public static Date selectedDate;
    protected boolean isQueryExecuted = false;
    protected Class<? extends EventCreationFragment> eventCreationFragmentClass = EventCreationFragment.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_calendar_activity);
        Bundle bundle;
        if(savedInstanceState == null){
            bundle = getIntent().getExtras();
        }else{
            bundle = savedInstanceState;
        }
        listview = (ListView) findViewById(R.id.listview);
        statusText = (TextView) findViewById(R.id.statusText);
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        dateClicked = bundle.getString("date");

        if( dateClicked != null ) {
            try {
                selectedDate = simpleDateFormat.parse(dateClicked);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        initialize();
    }

    /**
     * Initialize controllers and communication with Message Broker
     */
    protected void initialize(){
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        controller = CalendarController.getInstance( this );
        getCalendarEvents(selectedDate, 0);
        events = new ArrayList<>( );
        deleteEventsList = new ArrayList<>();
        eventsAdapter = new CalendarEventAdapter( this, R.layout.services_adapter_events, events );
        eventsAdapter.setCalendarDayDetailActivity(this);
        listview.setAdapter(eventsAdapter);
    }

    @Override
    protected void onResume() {
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        super.onResume();
    }

    @Override
    protected void onPause(){
        MessageBroker.getInstance( getApplicationContext() ).unsubscribe(this);
        super.onPause();
    }

    /**
     * release resources
     **/
    @Override
    protected void onDestroy(){
        MessageBroker.getInstance( getApplicationContext() ).unsubscribe(this);
        super.onDestroy();
    }

    /**
     * 2. DELETE CALENDAR EVENTS
     **/
    public void deleteEvents(View view){
        Toast.makeText( this, "Removing Events", Toast.LENGTH_LONG).show();
        deleteEvents( this, deleteEventsList, events );
    }

    protected void deleteEvents(Object caller, List<CalendarEventVO> deleteEventsList,
                                List<CalendarEventVO> events){
        controller.deleteEvents(caller, deleteEventsList, events);
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    public void onEventMainThread( CalendarNotificationEvent event ){
        if( event.isError() ){
            Toast.makeText( this, event.getNotification(), Toast.LENGTH_LONG).show();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_CREATE_EVENT ) ){
            refreshData( event.getEvents() );
            // you can also process only new events:
            // event.getNewEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_UPDATE_EVENT) ){
            refreshData(event.getEvents());
            // you can also process only updated events:
            // event.getUpdatedEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_QUERY_EVENTS ) ){
            refreshData( event.getEvents() );
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_EVENTS) ) {
            removeEvents();
            refreshData( event.getEvents() );
            // you can also process only the deleted events:
            // event.getDeletedEvents();
        }
        else if( event.getNotification().equals(Constants.CALENDAR_AVAILABILITY_EXCEPTION) ){
            controller.showGooglePlayServicesAvailabilityErrorDialog((Integer) event.getParams(),
                    CalendarDayDetailActivity.this);
        }else{
            Toast.makeText( this, event.getNotification(), Toast.LENGTH_LONG ).show();
        }
        checkGoBack();
    }


    /**
     * Event Handler for google play services
     * @param event
     */
    public void onEventMainThread(GooglePlayServicesEvent event){
        if( event.isError() ){
            Toast.makeText( getApplicationContext(), event.getNotification(),
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void refreshData(List<CalendarEventVO> eventList){
        events = new ArrayList<>(eventList);
        if (events == null ) {
            events = new ArrayList<>();
        }
        if (events.size() == 0) {
            statusText = (TextView) findViewById(R.id.statusText);
            statusText.setText("No data found.");
        }
        eventsAdapter.clear();
        eventsAdapter.addAll(events);
        eventsAdapter.notifyDataSetChanged();
    }


    public void createEventFragment(View v) {
        fragment = Util.createInstance( eventCreationFragmentClass, this,
                eventToModify == null? CalendarEventVO.class : eventToModify, isModifyEvent);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.calendarRelative);
        mainLayout.setVisibility(View.INVISIBLE);
        ft = fm.beginTransaction();
        ft.replace(R.id.calendarMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
        goBack = true;
    }

    public synchronized List<CalendarEventVO> getEvents() {
        return events;
    }

    public boolean isRearrangeElements() {
        return isRearrangeElements;
    }

    public static void setIsRearrangeElements(boolean isRearrangeElements) {
        CalendarDayDetailActivity.isRearrangeElements = isRearrangeElements;
    }

    public static void setIsModifyEvent(boolean isModifyEvent) {
        CalendarDayDetailActivity.isModifyEvent = isModifyEvent;
    }

    public static void setEventToModify(CalendarEventVO eventToModify) {
        CalendarDayDetailActivity.eventToModify = eventToModify;
    }

    public static List<CalendarEventVO> getDeleteEventsList() {
        return deleteEventsList;
    }

    public void onBackPressed() {
        goBack = false;
        if( isModifyEvent ){
            isModifyEvent = false;
            eventToModify = null;
        }
        if ( fm.getBackStackEntryCount() == 0 ) {
            super.onBackPressed();
        } else {
            fm.popBackStack();
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.calendarRelative);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns calendar events for a specific date. The results are handled on
     * onEvent(CalendarNotificationEvent event) method.
     * @param forSpecificDate if not null, it only returns calendar events for the specified date,
     *                        otherwise this date is not taken into account for filtering calendar
     *                        events.
     * @param numOfMonths  it returns all the calendar events that are scheduled for the next specified
     *                     amount of months. If -1, it returns calendar events for the next default
     *                     amount of months (by default 12 months), if 0 it returns only calendar events
     *                     for the specified date
     */
    public void getCalendarEvents(Date forSpecificDate, int numOfMonths){
        controller.getCalendarEvents(this, forSpecificDate, numOfMonths);
    }

    protected void checkGoBack(){
        if( goBack ) {
            CalendarDayDetailActivity.this.onBackPressed();
        }
    }

    protected void removeEvents(){
        deleteEventsList.clear();
        isRearrangeElements = true;
    }

    public void setQueryExecuted(boolean queryExecuted) {
        isQueryExecuted = queryExecuted;
    }

    public boolean isQueryExecuted() {
        return isQueryExecuted;
    }

    public void setEventCreationFragmentClass(Class eventCreationFragmentClass) {
        this.eventCreationFragmentClass = eventCreationFragmentClass;
    }
}
