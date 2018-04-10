package com.yahoo.inmind.testing.middleware;


import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.services.weather.control.WeatherProposition;
import edu.cmu.adroitness.client.services.weather.control.WeatherService;
import edu.cmu.adroitness.client.services.weather.model.DayWeatherVO;
import edu.cmu.adroitness.client.services.weather.model.HourWeatherVO;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.commons.rules.control.DecisionRuleValidator;
import edu.cmu.adroitness.commons.rules.model.DecisionRule;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WeatherTest {
    private static final String TAG = "CalendarTest";
    private static Context context;
    private static MessageBroker mb;
    private static ResourceLocator locator;
    private static WeatherService weather;
    private static DecisionRuleValidator validator;
    private static DecisionRule decisionRule;
    private static WeatherProposition proposition;
    private static HourWeatherVO hourWeatherVO;
    private static DayWeatherVO dayWeatherVO;


    @BeforeClass
    public static void setup(){
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        mb = MessageBroker.getInstance( context );
        locator = ResourceLocator.getInstance( context );
        do {
            weather = locator.lookupService(WeatherService.class);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while( weather == null );

        validator = DecisionRuleValidator.getInstance();
    }

    @AfterClass
    public static void teardown(){
        mb.destroy();
    }



    @Test
    /**
     * rule condition: if calendar event starts tomorrow at 9:00 am
      */
    public void testStartsTomorrowAtTime() {
//        decisionRule = new DecisionRule();
//        proposition = new WeatherProposition();
//        proposition.setComponentName( Constants.WEATHER  );
//
//        proposition.setAttribute( Constants.CALENDAR_START_TIME );
//        proposition.setOperator( Constants.OPERATOR_TIME_EQUAL );
//        proposition.setValue( "9:00" );
//        proposition.setRefAttribute( Constants.CLOCK_TOMORROW );
//        decisionRule.addCondition( "p1", proposition );
//
//        // rule action: show a toast message
//        decisionRule.addAction("a1", attributes);
//        String json = Util.toJson( decisionRule );
//
//        //cleaning calendar events. If there are no events then validator returns false
//        calendar.processEvents( Constants.CALENDAR_DELETE_ALL_EVENTS, null );
//        assertFalse( validator.createRule("CalendarRule", json, null, null) );
//        validator.unregisterRule( "CalendarRule" );
//
//        //create a correct event
//        eventVO = createEventVO();
//        Date start = Util.getRelativeDate( Calendar.DAY_OF_MONTH, 1);
//        start = Util.getTime( start, 9, 0 );
//        eventVO.setStartDate( start );
//        eventVO.setEndDate( Util.getRelativeDate(start, Calendar.HOUR, 1) );
//        calendar.processEvents( Constants.CALENDAR_INSERT_EVENT, eventVO );
//        assertTrue( validator.createRule("CalendarRule", json, null, null) );
//
//        //create a wrong event
//        proposition.setValue("11:00");
//        decisionRule.setConditions( new ArrayList<DecisionRule.ConditionElement>() );
//        decisionRule.addCondition("p1", proposition);
//        json = Util.toJson(decisionRule);
//        assertFalse( validator.createRule("CalendarRule", json, null, null) );
    }

}
