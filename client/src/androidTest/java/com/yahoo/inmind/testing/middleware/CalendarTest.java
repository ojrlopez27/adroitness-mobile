package com.yahoo.inmind.testing.middleware;


import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.rules.control.DecisionRuleValidator;
import com.yahoo.inmind.commons.rules.model.DecisionRule;
import com.yahoo.inmind.services.calendar.control.CalendarProposition;
import com.yahoo.inmind.services.calendar.control.CalendarService;
import com.yahoo.inmind.services.calendar.model.CalendarEventVO;
import com.yahoo.inmind.commons.control.ResourceLocator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CalendarTest {

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
        mb = MessageBroker.getInstance(context, services);
        locator = ResourceLocator.getInstance(context);
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
}
