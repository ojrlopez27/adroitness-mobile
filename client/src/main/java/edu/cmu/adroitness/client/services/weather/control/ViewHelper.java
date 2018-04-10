package edu.cmu.adroitness.client.services.weather.control;

import android.app.Activity;
import android.media.RingtoneManager;

import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.generic.model.MBRequest;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.rules.model.DecisionRule;
import com.yahoo.inmind.services.weather.control.WeatherProposition;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    protected MessageBroker mMB;
    protected Activity mActivity;


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

    // ****************************** WEATHER ***********************************************

    /**
     * Change the forecast mode from daily to hourly or viceversa
     * @return
     */
    public int changeForecastMode( String place ) {
        return (Integer) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_WEATHER_CHANGE_FORECAST_MODE)
                .put(Constants.WEATHER_PLACE, place));
    }

    /**
     * Retrieves the weather data corresponding to the specified place.
     * @param place If place is null, then user's current location data is used
     * @param refreshTime If refreshTime is null, then forecast reports will be checked every hour
     *                    (by default). It is measured in miliseconds
     * @param forceRefresh If forceRefresh is null or false, forecast report won't be refreshed
     *                     until any "update rule" is triggered (e.g., the [refreshTime = 1hour] rule,
     *                     or the [low temp < 65˚F] rule, etc.)
     * @param rules If no weather rules are provided then an update will be sent every 30 min
     *              regardless if the weather conditions remain equal or not.
     */
    public void subscribeToWeatherReport(String place, Long refreshTime,
                                         Boolean forceRefresh, List rules){
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_WEATHER_SUBSCRIBE_TO_FORECAST)
                .put(Constants.WEATHER_PLACE, place)
                .put(Constants.WEATHER_REFRESH_TIME, refreshTime)
                .put(Constants.WEATHER_FORCE_REFRESH, forceRefresh)
                .put(Constants.WEATHER_CONDITION_RULES, rules));
    }

    public void unsubscribeToWeatherReport(String place){
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_WEATHER_UNSUBSCRIBE_TO_FORECAST)
                .put(Constants.WEATHER_PLACE, place));
    }

    public List getWeatherReport(String place, int forecastMode){
        return (List) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_WEATHER_GET_REPORT)
                .put(Constants.WEATHER_PLACE, place)
                .put(Constants.WEATHER_MODE, forecastMode));
    }

    /**
     * Retrieves the weather data corresponding to the specified location
     * @param city
     * @param country
     * @param countryCode
     * @param stateCode
     * @param subArea
     */
    public void subscribeToWeatherReport(String city, String country, String countryCode, String stateCode,
                                         String subArea){
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_WEATHER_SUBSCRIBE_TO_FORECAST)
                .put(Constants.LOCATION_CITY, city)
                .put(Constants.LOCATION_COUNTRY, country)
                .put(Constants.LOCATION_COUNTRY_CODE, countryCode)
                .put(Constants.LOCATION_STATE_CODE, stateCode)
                .put(Constants.LOCATION_SUB_AREA, subArea));
    }

    /**
     * Example of how to add a Decision Rule. In order to create a Weather Rule, you need to
     * subscribe to the weather report for the same place.
     */
    public void addRule() {
        DecisionRule rule = new DecisionRule();
        WeatherProposition prop1 = new WeatherProposition( Constants.WEATHER_HIGH_TEMP_ENG,
                Constants.OPERATOR_HIGHER_THAN, "70" );
        // if you don't specify the place, it will take Constants.LOCATION_CURRENT_PLACE as default,
        // otherwise you can pass a specific place (you should subsribe a weather report for the same place)
        prop1.setPlace( "Denver" );
        // if you don't specify date and time, then the rule will be applicable to any date and time
        Calendar calendar = Calendar.getInstance(); //Util.getRelativeCalendar( Calendar.DAY_OF_YEAR, 1);
        prop1.setYear( calendar.get(Calendar.YEAR ));
        prop1.setMonth( calendar.get(Calendar.MONTH) + 1 ); // Jan = 1, Feb = 2 ... Dec = 12
        prop1.setDay( calendar.get(Calendar.DAY_OF_MONTH ));
        prop1.setHour( calendar.get(Calendar.HOUR_OF_DAY ));
        prop1.setMinute( calendar.get(Calendar.MINUTE ));
        rule.addCondition( "prop1", prop1);

        // rule action 1: ring the alarm
        HashMap attributes = new HashMap<>();
        attributes.put( Constants.ALARM_CONDITION_AT, true);
        attributes.put( Constants.ALARM_REFERENCE_TIME, Constants.ALARM_TIME_NOW);
        attributes.put( Constants.ALARM_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        rule.addAction( Constants.ALARM, attributes);

        mMB.send(ViewHelper.this, MBRequest.build( Constants.MSG_CREATE_DECISION_RULE )
                .put(Constants.DECISION_RULE, rule));
    }
}
