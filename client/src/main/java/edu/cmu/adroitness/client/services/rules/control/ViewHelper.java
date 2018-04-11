package edu.cmu.adroitness.client.services.rules.control;

import android.app.Activity;

import java.util.HashMap;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.sensors.accelerometer.model.AccelerometerProposition;
import edu.cmu.adroitness.client.services.calendar.control.CalendarProposition;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.commons.rules.model.DecisionRule;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private Activity mActivity;
    String rule1_id = "rule1";
    String rule2_id = "rule2";


    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }


    // ****************************** CROWD RULES ***********************************************

    /**
     * This is an example of how we can create a crowd rule that triggers the alarm effector when the
     * the phone is free-falling towards the ground. you just need to register the rule, and it will
     * be automatically validated when new data from accelerometer comes in into the Middleware.
     * The results are caught by the event handler method (onEvent) in the DecisionRuleActivity
     * class.
     */
    public void createCrowdRules() {
        String jsonString = "{\n" +
                "  \"conditions\": [\n" +
                "    {\n" +
                "      \"proposition\": {\n" +
                "        \"componentName\": \"ACCELEROMETER\",\n" +
                "        \"attribute\": \"ACCELEROMETER_FREE_FALL\"\n" +
                "      },\n" +
                "      \"term\": \"x\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"actions\": [\n" +
                "    {\n" +
                "      \"attributes\": {\n" +
                "        \"ALARM_REFERENCE_TIME\": \"ALARM_TIME_NOW\",\n" +
                "        \"ALARM_CONDITION_AT\": true,\n" +
                "        \"ALARM_RINGTONE_TYPE\": 2\n" +
                "      },\n" +
                "      \"componentName\": \"ALARM\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"attributes\": {\n" +
                "        \"TOAST_MESSAGE\": \"Cell phone is falling towards the ground\"\n" +
                "      },\n" +
                "      \"componentName\": \"TOAST\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 1. we can create a decision rule by either passing a json string representation:
        mMB.send(ViewHelper.this,
                MBRequest.build( Constants.MSG_CREATE_DECISION_RULE )
                        .put( Constants.DECISION_RULE_JSON, jsonString )
                        .put( Constants.DECISION_RULE_ID, rule1_id ));

        // 2. or by passing one by one the rule elements:
//        MBRequest request = MBRequest.build( Constants.MSG_CREATE_DECISION_RULE )
//                        .put(Constants.DECISION_RULE_ID, rule2_id);
//
//        // rule condition1: if calendar event starts tomorrow at 9:00am
//        DecisionRule rule2 = new DecisionRule();
//        PropositionalStatement prop1 = new CalendarProposition();
//        prop1.setComponentName(Constants.CALENDAR);
//        prop1.setAttribute(Constants.CALENDAR_START_TIME);
//        prop1.setOperator(Constants.OPERATOR_TIME_EQUAL );
//        prop1.setValue("9:00");
//        prop1.setRefAttribute(Constants.CLOCK_TOMORROW);
//        rule2.addCondition( "calendar1", prop1);
//
//        // rule condition2: if calendar event 2 starts tomorrow at 10:00am
//        PropositionalStatement prop2 = new CalendarProposition();
//        prop2.setComponentName(Constants.CALENDAR);
//        prop2.setAttribute(Constants.CALENDAR_START_TIME);
//        prop2.setOperator(Constants.OPERATOR_TIME_EQUAL );
//        prop2.setValue("10:00");
//        prop2.setRefAttribute(Constants.CLOCK_TOMORROW);
//        rule2.addCondition( "calendar2", prop2);
//
//        // rule action 1: ring the alarm
//        HashMap attributes = new HashMap<>();
//        attributes.put( Constants.ALARM_CONDITION_AT, true);
//        attributes.put( Constants.ALARM_REFERENCE_TIME, Constants.ALARM_TIME_NOW);
//        attributes.put( Constants.ALARM_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
//        rule2.addAction( Constants.ALARM, attributes);
//
//        // rule action 2: show a toast message
//        attributes = new HashMap<>();
//        attributes.put(Constants.TOAST_MESSAGE, "There is a pending event calendar");
//        rule2.addAction( Constants.TOAST, attributes);
//
//        request.put(Constants.DECISION_RULE, rule2);
//        mMB.send(Red5StreamingController.this, request);
    }

    public void removeCrowdRules() {
        mMB.send(ViewHelper.this,  MBRequest.build(Constants.MSG_REMOVE_DECISION_RULE)
                .put(Constants.DECISION_RULE_ID, rule1_id) );

        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_REMOVE_DECISION_RULE)
                .put(Constants.DECISION_RULE_ID, rule2_id));
    }

    public void generateRules(Object condCompName, Object condAttribute, Object condOperator, String condValue,
                              Object condRefValue, Object accCompName, Object accAttribute1,
                              Object accVal1, Object accAttribute2, Object accVal2,
                              Object accAttribute3, Object accVal3) {

        MBRequest request = MBRequest.build( Constants.MSG_CREATE_DECISION_RULE )
                .put(Constants.DECISION_RULE_ID, rule1_id);

        DecisionRule rule = new DecisionRule();
        PropositionalStatement prop1 = condCompName.equals(Constants.CALENDAR)? new CalendarProposition()
                : new AccelerometerProposition();
        prop1.setComponentName( condCompName.toString() );
        prop1.setAttribute( condAttribute.toString() );
        prop1.setOperator( condOperator.toString() );
        prop1.setValue( condValue );
        prop1.setRefAttribute( condRefValue.toString() );
        rule.addCondition( "prop1", prop1);

        // rule action 1: ring the alarm
        HashMap attributes = new HashMap<>();
        attributes.put( accAttribute1, transformAction( accVal1 ) );
        attributes.put( accAttribute2, transformAction( accVal2 ) );
        attributes.put( accAttribute3, transformAction( accVal3 ) );
        rule.addAction( accCompName.toString(), attributes);

        request.put(Constants.DECISION_RULE, rule);
        mMB.send(ViewHelper.this, request);
    }

    private Object transformAction( Object action ){
        if( action.equals("ALARM_TYPE_NOTIFICATION") ){
            return 2;
        }
        return action;
    }
}
