package edu.cmu.adroitness.client.services.calendar.view;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.generic.model.GooglePlayServicesEvent;

/**
 * This is an example of how you can interact with calendar events:
 * 1. QUERY CALENDAR EVENTS
 * 2. DELETE ALL CALENDAR EVENTS
 */

public class ExtendedCalendarActivity extends CalendarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClassCalendarEventDetail( ExtendedCalendarDayDetailActivity.class );
    }

    @Override
    protected void initialize(){
        super.initialize();
        // optional: You can specify the synchronization frequency (in milliseconds) to sync with
        // the calendar server (for changes done out of the android app. e.g., in the gmail calendar
        // web site). By default is 2 min
        setSyncTime(30 * 1000L); // 30 seconds
    }

    /**
     * Called after AccountPicker and authorization dialog box exits.
     * @param requestCode code indicating which Activity result is incoming.
     * @param resultCode code indicating the result of the incoming Activity result.
     * @param data Intent (containing result data) returned by incoming Activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == Constants.REQUEST_ACCOUNT_PICKER ){
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 2. DELETE ALL CALENDAR EVENTS
     * This is called when user clicks on "delete all events" button
     **/
    @Override
    public void deleteAllEvents(View view){
       super.deleteAllEvents(view);
    }


    /**
     * This is an Event Handler. Here you can handle calendar events and error messages every time
     * that you make a request (e.g., a query) or when a calendar update happens.
     * @param event
     */
    @Override
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
            // You can also process only the list of deleted calendar events by doing:
            // event.getDeletedEvents();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_CREATE_EVENT ) ){
            refreshData( event.getAllEvents() );
            // You can also process only the list of new calendar events by doing:
            // event.getNewEvents();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_UPDATE_EVENT) ){
            refreshData(event.getAllEvents());
            // You can also process only the list of deleted calendar events by doing:
            // event.getUpdatedEvents();
            // ...
        }
        else if( event.getNotification().equals(Constants.CALENDAR_AVAILABILITY_EXCEPTION) ){
            // ...
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
    @Override
    public void onEventMainThread(GooglePlayServicesEvent event){
        if( event.isError() ){
            Toast.makeText( getApplicationContext(), event.getNotification(), Toast.LENGTH_LONG).show();
        }
    }
}
