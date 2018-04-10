package edu.cmu.adroitness.client.services.calendar.control;

import android.app.Activity;

import com.yahoo.inmind.comm.calendar.model.CalendarNotificationEvent;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.generic.model.MBRequest;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.services.calendar.model.CalendarEventVO;
import com.yahoo.inmind.services.calendar.view.EventCreationFragment;

import java.util.Date;
import java.util.List;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    protected EventCreationFragment eventCreationFragment;
    protected static ViewHelper instance;
    protected MessageBroker mMB;
    protected Activity mActivity;
    protected String accountName;

    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        // you have to subscribe the object who will handle the event responses (that is, those
        // implementing the onEventMainThread method.
        mMB.subscribe( activity );
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    public void setEventCreationFragment(EventCreationFragment eventCreationFragment) {
        this.eventCreationFragment = eventCreationFragment;
    }

    // ****************************** CALENDAR ***********************************************


    /**
     * We get a local copy of the calendar event from the service
     * @param forSpecificDate if null, it will bring all calendar events, otherwise only the events
     *                for the selected date
     * @param numberOfMonths if 0, it will return calendar events for the specified date (forSpecificDate)
     *                       if -1, it will return calendar events for the next amount of months by default
     *                       (i.e., 12 months). Otherwise, it will return calendar events for the
     *                       specific amount of months
     */
    public void getCalendarEvents(Object subscriber, Date forSpecificDate, int numberOfMonths) {
        mMB.sendAndReceive(subscriber, CalendarNotificationEvent.class,
                MBRequest.build(Constants.MSG_GET_CALENDAR_EVENTS)
                        .put(Constants.CALENDAR_FOR_SPECIFIC_DATE, forSpecificDate)
                        .put(Constants.CALENDAR_NUMBER_OF_MONTHS, numberOfMonths));
    }

    public void createCalendarEvent(Object caller, CalendarEventVO calendarEventVO) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_INSERT_EVENT)
                        .put(Constants.CALENDAR_EVENT_DATA, calendarEventVO));
    }

    public void updateCalendarEvent(Object caller, CalendarEventVO calendarEventVO) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_UPDATE_EVENT)
                        .put(Constants.CALENDAR_EVENT_DATA, calendarEventVO));
    }

    public void deleteEvents(Object caller, List<CalendarEventVO> deleteEventsList) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_DELETE_EVENTS)
                        .put(Constants.CALENDAR_EVENT_DATA, deleteEventsList));
    }

    public void deleteAllEvents(Object caller) {
        mMB.send(caller, MBRequest.build(Constants.MSG_PROCESS_EVENTS_CALENDAR)
                        .put(Constants.CALENDAR_MODE, Constants.CALENDAR_DELETE_ALL_EVENTS)
                        .put(Constants.CALENDAR_EVENT_DATA, null));
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
