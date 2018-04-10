package edu.cmu.adroitness.client.services.calendar.control;

import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscarr on 9/28/15.
 */
public class CalendarProposition extends PropositionalStatement {

    public CalendarProposition(){
        super();
        initialize(true);
    }

    public CalendarProposition(boolean subscribe){
        super();
        initialize(subscribe);
    }

    public CalendarProposition(String attribute, String operator, Object value) {
        super(attribute, operator, value.toString());
        initialize(true);
    }

    public CalendarProposition(String attribute, String operator, Object value, String refAttribute) {
        super(attribute, operator, value.toString(), refAttribute);
        initialize(true);
    }

    @Override
    public void initialize(boolean subscribe){
        super.initialize(subscribe);
        componentName = Constants.CALENDAR;
    }

    @Override
    public void postCreate() {
        //TODO
    }

    @Override
    public ArrayList validate() {
        return getList(ResourceLocator.getExistingInstance().lookupService(CalendarService.class)
                .getEvents());
    }

    @Override
    public Object validate(Object calendarEvent) {
        CalendarEventVO calendarEventVO = (CalendarEventVO) calendarEvent;
        if( attribute.equals(Constants.CALENDAR_START_TIME)
                || attribute.equals( Constants.CALENDAR_END_TIME)){
            return validateTimes( calendarEventVO );
        }else if( attribute.equals(Constants.CALENDAR_START_DATE)
                || attribute.equals( Constants.CALENDAR_END_DATE)){
            return validateDates( calendarEventVO );
        }
        return false;
    }
    

    public Boolean validateDates( CalendarEventVO calendarEventVO ){
        if( operator.equals( Constants.OPERATOR_DATE_BEFORE ) ){
            //return System.currentTimeMillis() - attributeDate.getTime() == ( Integer.valueOf(value) * 1000 );
        }else if( operator.equals( Constants.OPERATOR_DATE_AFTER ) ){
            //return System.currentTimeMillis() + attributeDate.getTime() == ( Integer.valueOf(value) * 1000 );
        }else if( operator.equals( Constants.OPERATOR_DATE_EQUAL ) ){
            if( value.equals( Constants.CLOCK_NOW)){
                //return new Date( System.currentTimeMillis() );
            }
        }
        return false;
    }


    /**
     * It returns a date/time when to schedule a process which will validateRule/process the action
     * @param calendarEventVO
     * @return
     */
    private Object validateTimes( CalendarEventVO calendarEventVO ){
        Date time1, time2 = null, now = new Date( System.currentTimeMillis() );
        time1 = attribute.equals( Constants.CALENDAR_START_TIME )? calendarEventVO.getStartDate()
            : attribute.equals( Constants.CALENDAR_END_TIME )? calendarEventVO.getEndDate() : now;
        if( refAttribute != null && refAttribute.equals( Constants.CALENDAR_END_TIME ) ){
            time2 = calendarEventVO.getEndDate();
        }else if( refAttribute != null && refAttribute.equals( Constants.CALENDAR_START_TIME ) ){
            time2 = calendarEventVO.getStartDate();
        }else if( refAttribute != null && refAttribute.equals( Constants.CLOCK_TODAY)){
            time2 = Util.getDateTime( new Date(), value);
        }else if( refAttribute != null && refAttribute.equals( Constants.CLOCK_TOMORROW)){
            time2 = Util.getRelativeDate( Util.getDateTime( new Date(), value), Calendar.DAY_OF_MONTH, 1);
        }

        long threshhold = 5 /* min */ * 60 /* sec */ * 1000 /* millis */;
        if( operator.equals( Constants.OPERATOR_TIME_BEFORE ) ){
            // time1 starts n minutes before now
            if( value.equals( Constants.CLOCK_NOW) && Util.isDateInRange(time1.getTime(),
                    threshhold, now.getTime()) ){
                return calendarEventVO;
            }
            // time1 starts n minutes before time2
            if( time2 != null && ( time2.getTime() + threshhold >= time1.getTime()
                    + (value.contains(":")? 0 : Long.valueOf(value).longValue() )) ){
                return calendarEventVO;
            }
        }else if( operator.equals( Constants.OPERATOR_TIME_AFTER ) ){
            // time1 is n minutes after now
            if( refAttribute.equals( Constants.CLOCK_NOW) && Util.isDateInRange(
                    time1.getTime(), threshhold, now.getTime() + Long.valueOf( value ) ) ){
                return calendarEventVO;
            }
            // time1 n minutes after time2
            if( time2 != null && ( time2.getTime() + Long.valueOf( value ) == time1.getTime() ) ){
                return calendarEventVO;
            }
        }else if( operator.equals( Constants.OPERATOR_TIME_EQUAL ) ){
            if( value.equals( Constants.CLOCK_NOW) &&
                    Util.isDateInRange( time1.getTime(), threshhold, now.getTime() )) {
                return calendarEventVO;
            }

            // time2 is today/tomorrow at time: "value"
            if( ( refAttribute.equals( Constants.CLOCK_TODAY) ||  refAttribute.equals( Constants.CLOCK_TOMORROW) )
                    && Util.isDateInRange(
                        time1.getTime(), threshhold, time2.getTime() ) ){
                return calendarEventVO;
            }
        }
        return null;
    }


    public void onEvent( CalendarNotificationEvent event ){
        ArrayList list =  new ArrayList( event.getNewEvents() );
        list.addAll( event.getUpdatedEvents() );
        super.onEvent( list, event.getDeletedEvents() );
    }

}
