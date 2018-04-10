package edu.cmu.adroitness.client.services.calendar.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.GooglePlayServicesEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.calendar.control.CalendarController;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;

import edu.cmu.adroitness.R;

/**
 * This is an example of how you can operate over calendar events:
 * 1. QUERY CALENDAR EVENTS
 * 2. DELETE ALL CALENDAR EVENTS
 */
public class CalendarActivity extends AppCompatActivity {
    private CalendarController controller;
    protected static List<CalendarEventVO> events;
    protected static CompactCalendarView monthCalendarView;
    protected static String dateSelected;
    protected static Toolbar decoration_toolbar;
    private Class<? extends CalendarDayDetailActivity> classCalendarEventDetail = CalendarDayDetailActivity.class;
    protected boolean isQueryExecuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.services_calendar_main);
        decoration_toolbar = (Toolbar) findViewById(R.id.decoration_toolbar);
        monthCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        events = new ArrayList<>();
        initialize();

        /**** Set listener for Calendar on Key Press (click on date) ***/
        monthCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateSelected = Util.getDate(dateClicked, "MM/dd/yyyy");
                Intent intent = new Intent(CalendarActivity.this, classCalendarEventDetail);
                Bundle bundle = new Bundle();
                bundle.putString("date", dateSelected);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                decoration_toolbar.setTitle(Util.getDate(firstDayOfNewMonth, "MMM - yyyy"));
                monthCalendarView.drawSmallIndicatorForEvents(true);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("dateVal", dateSelected);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialize controllers and communication with Message Broker
     */
    protected void initialize(){
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        controller = CalendarController.getInstance( this );
        // it returns a result to onActivityResult method
        controller.chooseAccount(this);
        setSyncTime(100 * 60 * 1000L); // 10 seconds
        monthCalendarView.setClickable(true);
        decoration_toolbar.setTitle(Util.getDate(monthCalendarView.getFirstDayOfCurrentMonth(), "MMM - yyyy"));
        if( controller.getAccountName() != null ){
            controller.getCalendarEvents( this, null, -1);
        }
    }

    protected void onResume() {
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        super.onResume();
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
     * Called when an mActivity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which mActivity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     mActivity result.
     * @param data Intent (containing result data) returned by incoming
     *     mActivity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        configAccountName(accountName);
                        /** 1. QUERY CALENDAR EVENTS **/
                        getCalendarEvents(null, -1);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Account unspecified.", Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    //controller.chooseAccount(this);
                    controller.getCalendarEvents( this, null, -1);
                }
                break;
            case Constants.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    controller.checkGooglePlayServicesAvailable();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 2. DELETE ALL CALENDAR EVENTS
     **/
    public void deleteAllEvents(View view){
        Toast.makeText( CalendarActivity.this, "Removing All Events", Toast.LENGTH_LONG).show();
        controller.deleteAllEvents(CalendarActivity.this);
        monthCalendarView.removeAllEvents();
    }


    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    public void onEventMainThread( final CalendarNotificationEvent event ){

        if( event.isError() ){
            Toast.makeText( this, event.getNotification(), Toast.LENGTH_LONG).show();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_QUERY_EVENTS ) ){
            refreshData(event.getAllEvents());
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_ALL_EVENTS ) ){
            refreshData(event.getAllEvents());
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_EVENTS) ) {
            refreshData( event.getAllEvents() );
            // you can also process only the deleted events:
            // event.getDeletedEvents();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_CREATE_EVENT ) ){
            // here is the list of events after insertion
            refreshData( event.getAllEvents() );
            // you can also process only new events:
            // event.getNewEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_UPDATE_EVENT) ){
            // here is the list of events after update
            refreshData(event.getAllEvents());

            // you can also process only updated events:
            // event.getUpdatedEvents();
            // ...
        }
        else if( event.getNotification().equals(Constants.CALENDAR_AVAILABILITY_EXCEPTION) ){
            controller.showGooglePlayServicesAvailabilityErrorDialog((Integer) event.getParams(),
                    CalendarActivity.this);
        }else if( event.getNotification().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION) ){
            startActivityForResult((Intent) event.getParams(), Constants.REQUEST_AUTHORIZATION);
        }else{
            Toast.makeText(this, event.getNotification(), Toast.LENGTH_LONG).show();
        }
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
        Toast.makeText(this, "Retrieving data...",Toast.LENGTH_LONG).show();
        events = new ArrayList<>(eventList);
        if (events == null ) {
            events = new ArrayList<>();
        }
        if (events.size() == 0) {
            Toast.makeText(this, "No events found.",Toast.LENGTH_LONG).show();
            monthCalendarView.removeAllEvents();
            monthCalendarView.drawSmallIndicatorForEvents(true);
        } else {
            monthCalendarView.removeAllEvents();
            monthCalendarView.addEvents( events );
            monthCalendarView.drawSmallIndicatorForEvents(false);
        }
    }

    public Class<? extends CalendarDayDetailActivity> getClassCalendarEventDetail() {
        return classCalendarEventDetail;
    }

    public void setClassCalendarEventDetail(Class<? extends CalendarDayDetailActivity> classCalendarEventDetail) {
        this.classCalendarEventDetail = classCalendarEventDetail;
    }

    public void setQueryExecuted(boolean queryExecuted) {
        isQueryExecuted = queryExecuted;
    }

    public boolean isQueryExecuted() {
        return isQueryExecuted;
    }

    /**
     * time in miliseconds to sync with the calendar server (for changes done out of
     * the android app. e.g., in the calendar web site). By default is 2 min
     * @param syncTime
     */
    public void setSyncTime(long syncTime) {
        controller.setSyncTime(syncTime);
    }

    public void configAccountName(String accountName){
        controller.configAccountName(this, accountName);
    }

    /**
     * Returns calendar events for a specific date. The results are handled on
     * onEventMainThread(CalendarNotificationEvent event) method.
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
}
