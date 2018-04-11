package edu.cmu.adroitness.client.services.calendar.view;

import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.calendar.control.ViewHelper;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomizedCalendarActivity extends AppCompatActivity {
    private ViewHelper helper;
    private CalendarEventVO calendarEventVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_calendar_customized);
        helper = ViewHelper.getInstance( this );
        helper.chooseAccount(this);
    }

    public void queryCalendarEvents( View view){
        // this will returns all the calendar events for the next one year
        helper.getCalendarEvents( this, null, -1);
        // this will returns all the calendar events for the next 3 months
        helper.getCalendarEvents( this, null, 3);
        // this will returns all calendar events for the specified date (today)
        helper.getCalendarEvents( this, new Date(), 0);
    }

    public void createCalendarEvent( View view ){
        // let's create a calendar event for tomorrow at 9:00 am
        Date startDate = Util.getRelativeDate(Calendar.DAY_OF_MONTH, 1);
        String startTime = "9:00";
        Date endDate = startDate;
        String endTime = "10:00";
        calendarEventVO = createEventVO( startDate, startTime, endDate, endTime);
        helper.createCalendarEvent( this, calendarEventVO);
    }

    public void updateCalendarEvent( View view ){
        calendarEventVO.setSummary( "Now, we modify the event's summary ");
        helper.updateCalendarEvent( this, calendarEventVO);
    }

    public void deleteCalendarEvent( View view ){
        ArrayList<CalendarEventVO> deleteEventsList = new ArrayList<>();
        deleteEventsList.add( calendarEventVO );
        helper.deleteEvents( this, deleteEventsList );
    }

    public void deleteAllCalendarEvents( View view ){
        helper.deleteAllEvents( this );
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( CalendarNotificationEvent event ){
        if( event.isError() ){
            Toast.makeText( getApplicationContext(), event.getNotification(), Toast.LENGTH_LONG).show();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_CREATE_EVENT ) ){
            // You can process only the list of new calendar events by doing:
            event.getNewEvents();
            // for instance:
            calendarEventVO = event.getNewEvents().get(0);
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_UPDATE_EVENT) ){
            // You can process only the list of deleted calendar events by doing:
            event.getUpdatedEvents();
            // ...
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_QUERY_EVENTS ) ){
            // retrieves all the calendar events for the specified date
            event.getEvents();
            // you can also process the WHOLE set of calendar events
            event.getAllEvents();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_EVENTS) ) {
            // You can process only the list of deleted calendar events by doing:
            event.getDeletedEvents();
        }
        else if( event.getNotification().equals( Constants.CALENDAR_AFTER_DELETE_ALL_EVENTS) ) {
            // You can process only the list of all deleted calendar events by doing:
            event.getDeletedEvents();
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
    }


    /**
     * Called after AccountPicker and authorization dialog box exits. You willl need this just in
     * case the google acount hasn't been set.
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
                    helper.configAccountName(this, accountName);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account unspecified.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected CalendarEventVO createEventVO( Date startDate, String startTime, Date endDate,
                                             String endTime ){
        CalendarEventVO event = new CalendarEventVO()
                .setSummary( "Meeting with InMind folks" )
                .setLocation( "Room 8015, GHC, CMU" )
                .setDescription( "Meeting to discuss about deadlines" );
        event.setStartDate( Util.getDateTime(startDate, startTime));
        event.setEndDate( Util.getDateTime(endDate, endTime));

        ArrayList<String> attendees = new ArrayList<>();
        attendees.add("somebody@cs.cmu.edu");
        attendees.add("somebody@yahoo-inc.com");
        event.setAttendees( attendees );
        event.setEmailReminder(24 * 60); // one day in advance
        event.setSmsReminder(10); // 10 minutes in advance
        event.setNumberOfMonths(0);
        return event;
    }
}
