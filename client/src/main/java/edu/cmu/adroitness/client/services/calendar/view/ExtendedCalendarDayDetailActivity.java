package edu.cmu.adroitness.client.services.calendar.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.model.GooglePlayServicesEvent;

/**
 * These are the operations supported by this class:
 * 1. QUERY CALENDAR EVENTS (By selected date)
 * 2. DELETE CALENDAR EVENTS
 */
public class ExtendedCalendarDayDetailActivity extends CalendarDayDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initialize controllers and communication with Message Broker. Also, it allows:
     * 1. QUERY CALENDAR EVENTS
     */
    @Override
    protected void initialize(){
        super.initialize();
        getCalendarEvents(selectedDate, 0);
        setEventCreationFragmentClass( ExtendedCalendarFragment.class );
    }

    /**
     * 2. DELETE CALENDAR EVENTS
     **/
    @Override
    public void deleteEvents(View view){
        Toast.makeText( this, "Removing Events", Toast.LENGTH_LONG).show();
        deleteEvents(this, deleteEventsList, events);
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( CalendarNotificationEvent event ){
        if( event.isError() ){
            Toast.makeText( this, event.getNotification(), Toast.LENGTH_LONG).show();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_CREATE_EVENT ) ){
            // event.getEvents() retrieves all the calendar events for the current selected date
            refreshData( event.getEvents() );
            // You can also process only the list of new calendar events by doing:
            // event.getNewEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_UPDATE_EVENT) ){
            // event.getEvents() retrieves all the calendar events for the current selected date
            refreshData(event.getEvents());
            // You can also process only the list of deleted calendar events by doing:
            // event.getUpdatedEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_QUERY_EVENTS ) ){
            refreshData( event.getEvents() );
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_EVENTS) ) {
            removeEvents();
            refreshData( event.getEvents() );
            // You can also process only the list of deleted calendar events by doing:
            // event.getDeletedEvents();
        }
        else if( event.getNotification().equals(Constants.CALENDAR_AVAILABILITY_EXCEPTION) ){
            //...
        }
        else if(event.getNotification().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION))
        {
            startActivityForResult((Intent) event.getParams(), Constants.REQUEST_AUTHORIZATION);
        }
        else{
            Toast.makeText( this, event.getNotification(), Toast.LENGTH_LONG ).show();
        }
        checkGoBack();
    }

    /**
     * Event Handler for google play services
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GooglePlayServicesEvent event){
        if( event.isError() ){
            Toast.makeText( getApplicationContext(), event.getNotification(), Toast.LENGTH_LONG).show();
        }
    }
}
