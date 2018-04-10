package com.yahoo.inmind.testing.middleware;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.commons.control.Util;
import com.yahoo.inmind.commons.rules.control.DecisionRuleValidator;
import com.yahoo.inmind.commons.rules.model.DecisionRule;
import com.yahoo.inmind.commons.rules.model.PropositionalStatement;
import com.yahoo.inmind.services.calendar.control.CalendarProposition;
import com.yahoo.inmind.services.calendar.control.CalendarService;
import com.yahoo.inmind.services.calendar.model.CalendarEventVO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RuleValidationCalendarTest {
    private static final String TAG = "RuleValidationCalendarTest";
    private static Context context;
    private static MessageBroker mb;
    private static ResourceLocator locator;
    private static CalendarService calendar;
    private static DecisionRuleValidator validator;
    private static DecisionRule decisionRule;
    private static CalendarProposition proposition;
    private static CalendarEventVO eventVO;
    private static HashMap attributes;


    @BeforeClass
    public static void setup(){
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add( Constants.ADD_SERVICE_CALENDAR );
        mb = MessageBroker.getInstance( context, services );
        locator = ResourceLocator.getInstance( context );
        do {
            calendar = locator.lookupService(CalendarService.class);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while( calendar == null );

        validator = DecisionRuleValidator.getInstance();
        attributes = new HashMap();
        attributes.put(Constants.ACTION_TYPE, Constants.TOAST);
        attributes.put(Constants.TOAST_MESSAGE, "There is a pending event calendar");
    }

    @AfterClass
    public static void teardown(){
        mb.destroy();
    }


    //@Test
    public void testDeleteEvents(){
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, createEventVO(), -1 );
        assertTrue( calendar.getEvents().size() > 0 );
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );
        assertTrue( calendar.getEvents().size() == 0 );
    }


    //@Test
    /**
     * rule condition: if calendar event starts tomorrow at 9:00 am
     */
    public void testStartsTomorrowAtTime() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_START_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_EQUAL );
        proposition.setValue( "9:00" );
        proposition.setRefAttribute( Constants.CLOCK_TOMORROW );
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning calendar events. If there are no events then validator returns false
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );
        assertFalse( validator.createRule("CalendarRule", json, null) );
        validator.unregisterRule( "CalendarRule" );

        //create a correct event
        eventVO = createEventVO();
        Date start = Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1);
        start = Util.getTime( start, 9, 0 );
        eventVO.setStartDate( start );
        eventVO.setEndDate( Util.getRelativeDate(start, Calendar.HOUR, 1) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong event
        proposition.setValue("11:00");
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }


    //@Test
    /**
     * If calendar event starts 10 minutes from now
     */
    public void testAfterSomeTimeFromNow() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_START_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_AFTER );
        proposition.setValue( "600000" );
        proposition.setRefAttribute( Constants.CLOCK_NOW );
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );

        //create a correct event
        eventVO = createEventVO();
        eventVO.setStartDate( Util.getRelativeDate( eventVO.getStartDate(), Calendar.MINUTE, 10) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong event (50 minutes from now)
        proposition.setValue("3000000");
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }



    //@Test
    /**
     * If Calendar event starts 30 minutes before event's end time
     */
    public void testStartsBeforeEndTime() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_START_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_BEFORE );
        proposition.setValue( "1800000" );
        proposition.setRefAttribute( Constants.CALENDAR_END_TIME );
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );

        //create a correct event
        eventVO = createEventVO();
        eventVO.setEndDate( Util.getRelativeDate( eventVO.getStartDate(), Calendar.MINUTE, 30) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong event (50 minutes before)
        proposition.setValue("3000000");
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }



    //@Test
    /**
     * If Calendar event ends 120 minutes after event's start time
     */
    public void testEndsTimeAfterStartTime() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_END_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_AFTER);
        proposition.setValue( "" + 120 * 60 * 1000 );
        proposition.setRefAttribute( Constants.CALENDAR_START_TIME);
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );

        //create a correct event
        eventVO = createEventVO();
        eventVO.setStartDate( Util.getTime( eventVO.getStartDate(), 20, 00) );
        eventVO.setEndDate( Util.getTime( eventVO.getEndDate(), 22, 00) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong rule (100 minutes after)
        proposition.setValue("" + 100 * 60 * 1000);
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }


    //@Test
    /**
     * If Calendar event starts 23:30 today
     */
    public void testStartsSpecificTimeToday() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_START_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_EQUAL);
        proposition.setValue( "23:30" );
        proposition.setRefAttribute( Constants.CLOCK_TODAY );
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );

        //create a correct event
        eventVO = createEventVO();
        eventVO.setStartDate( Util.getTime( eventVO.getStartDate(), 23, 30) );
        eventVO.setEndDate( Util.getTime( eventVO.getEndDate(), 23, 45) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong rule (22:30)
        proposition.setValue("22:30");
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }


    //@Test
    /**
     * If Calendar event starts now
     */
    public void testStartsNow() {
        decisionRule = new DecisionRule();
        proposition = new CalendarProposition(false);
        proposition.setComponentName( Constants.CALENDAR  );
        proposition.setAttribute( Constants.CALENDAR_START_TIME );
        proposition.setOperator( Constants.OPERATOR_TIME_EQUAL);
        proposition.setValue( Constants.CLOCK_NOW );
        decisionRule.addCondition( "p1", proposition );

        // rule action: show a toast message
        decisionRule.addAction("a1", attributes);
        String json = Util.toJsonPretty( decisionRule );

        //cleaning
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );

        //create a correct event
        eventVO = createEventVO();
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        assertTrue( validator.createRule("CalendarRule", json, null) );

        //create a wrong rule (22:30)
        proposition.setValue("22:30");
        proposition.setRefAttribute( Constants.CLOCK_TODAY );
        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
        decisionRule.addCondition("p1", proposition);
        json = Util.toJsonPretty(decisionRule);
        assertFalse( validator.createRule("CalendarRule", json, null) );
    }


    @Test
    /**
     * There are two calendar events, a new rule is created and matches the calendar events. The
     * rule's actions are triggered. After 2 minutes (periodically validation, by default 24 hours)
     * the rule should be triggered again.
     */
    public void testRightAfterCreatingARule(){
        //wait 5 sec for getDataFromApi
        Util.sleep(5 * 1000L);

        //cleaning calendar events
        //calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null, -1 );
        Util.sleep( 5 * 1000 );

        //create calendar events
        eventVO = createEventVO();
        eventVO.setStartDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 9, 0) );
        eventVO.setEndDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 10, 0) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        Util.sleep( 5 * 1000 );
        eventVO.setStartDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 10, 0) );
        eventVO.setEndDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 11, 0) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        Util.sleep( 5 * 1000 );

        //create rule
        decisionRule = new DecisionRule();
        // rule condition1: if calendar event starts tomorrow at 9:00am
        proposition = new CalendarProposition(false);
        proposition.setComponentName(Constants.CALENDAR);
        proposition.setAttribute(Constants.CALENDAR_START_TIME);
        proposition.setOperator(Constants.OPERATOR_TIME_EQUAL );
        proposition.setValue("9:00");
        proposition.setRefAttribute(Constants.CLOCK_TOMORROW);
        decisionRule.addCondition( "calendar1", proposition);

        // rule condition2: if calendar event 2 starts tomorrow at 10:00am
        PropositionalStatement prop2 = new CalendarProposition();
        prop2.setComponentName(Constants.CALENDAR);
        prop2.setAttribute(Constants.CALENDAR_START_TIME);
        prop2.setOperator(Constants.OPERATOR_TIME_EQUAL );
        prop2.setValue("10:00");
        prop2.setRefAttribute(Constants.CLOCK_TOMORROW);
        decisionRule.addCondition( "calendar2", prop2);

        // rule action 1: ring the alarm
        HashMap attributes = new HashMap<>();
//        attributes.put( Constants.ALARM_CONDITION_AT, true);
//        attributes.put( Constants.ALARM_REFERENCE_TIME, Constants.ALARM_TIME_NOW);
//        attributes.put( Constants.ALARM_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
//        decisionRule.addAction( Constants.ALARM, attributes);

        // rule action 2: show a toast message
        attributes = new HashMap<>();
        attributes.put(Constants.TOAST_MESSAGE, "There is a pending event calendar");
        decisionRule.addAction( Constants.TOAST, attributes);

        // validate rule (it should trigger actions)
        validator.setValidationTimer( 2 * 60 * 1000L); //2 minutes
        assertTrue( validator.createRule("CalendarRule", null, decisionRule) );

        //remove calendar event
        for(CalendarEventVO eventTemp : calendar.getEvents()){
            if( Util.getTime( eventTemp.getStartDate() ).substring(0, 2).equals("10") ){
                eventVO.setId( eventTemp.getId() );
            }
        }
        calendar.processEvents( Constants.CALENDAR_DELETE_EVENTS, eventVO, -1 );
        Util.sleep( 30 * 1000);
        assertFalse( validator.validateRule( decisionRule ));

        //create a calendar event (it should trigger actions)
        eventVO.setId(null);
        eventVO.setStartDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 10, 0) );
        eventVO.setEndDate( Util.getTime( Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1), 11, 0) );
        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO, -1 );
        Util.sleep( 20 * 1000 );
        assertTrue( validator.validateRuleIsTrue( decisionRule ));

        //update calendar event
        eventVO = calendar.getEvents().get(0);
        eventVO.setSummary( "Updated Summary" );
        calendar.processEvents( Constants.CALENDAR_UPDATE_EVENT, eventVO, -1 );
        Util.sleep( 5 * 1000 );
        assertFalse( validator.validateRule( decisionRule ));


        //wait 3 minutes for ValidationTimerTask
        //sleep(3 * 60 * 1000L);
    }


    private CalendarEventVO createEventVO( ){
        CalendarEventVO event = new CalendarEventVO()
                .setSummary( "summary" )
                .setLocation( "location" )
                .setDescription( "description" );
        event.setStartDate( new Date() );
        event.setEndDate( Util.getRelativeDate( Calendar.MINUTE, 30) );
        ArrayList<String> attendees = new ArrayList<>();
        attendees.add("somebody@cs.cmu.edu");
        attendees.add("somebody@yahoo-inc.com");
        event.setAttendees( attendees );
        event.setEmailReminder(24 * 60); // one day in advance
        event.setSmsReminder(10); // 10 minutes in advance
        return event;
    }
}
