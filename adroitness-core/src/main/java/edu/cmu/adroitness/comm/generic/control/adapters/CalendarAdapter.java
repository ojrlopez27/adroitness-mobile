package edu.cmu.adroitness.comm.generic.control.adapters;

import java.util.Date;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.calendar.control.CalendarService;

/**
 * Created by oscarr on 3/15/16.
 */
public final class CalendarAdapter extends ChannelAdapter{
    private static CalendarAdapter instance;

    private CalendarAdapter() {
        super();
    }

    public static CalendarAdapter getInstance() {
        if (instance == null) {
            instance = new CalendarAdapter();
        }
        return instance;
    }

    public void processEvents( MBRequest mbRequest){
        mResourceLocator.addRequest( CalendarService.class, "processEvents",
                mbRequest.get(Constants.CALENDAR_MODE),
                mbRequest.get(Constants.CALENDAR_EVENT_DATA),
                mbRequest.hashCode() );
    }

    public void getCalendarEvents( MBRequest mbRequest){
        //mbRequest.put(Constants.CALENDAR_MBREQUEST_ID, mbRequest.hashCode());
        mResourceLocator.addRequest( CalendarService.class, "getCalendarEvents",
                ((Date) mbRequest.get(Constants.CALENDAR_FOR_SPECIFIC_DATE)),
                ((Integer) mbRequest.get(Constants.CALENDAR_NUMBER_OF_MONTHS)),
                ((Integer)mbRequest.hashCode() ));
    }


    public void setCalendarSyncTime( MBRequest mbRequest){
        mResourceLocator.addRequest( CalendarService.class,"setSyncTime",
                ((Long) mbRequest.get(Constants.CALENDAR_SYNC_TIME)));
    }
}
