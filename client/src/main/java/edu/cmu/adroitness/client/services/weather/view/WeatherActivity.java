package edu.cmu.adroitness.client.services.weather.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.generic.view.ServicesActivity;
import edu.cmu.adroitness.client.services.weather.control.ViewHelper;
import edu.cmu.adroitness.client.services.weather.control.WeatherProposition;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.location.model.LocationEvent;
import edu.cmu.adroitness.comm.weather.model.WeatherEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * You will be able to:
 * 1. SUBSCRIBE (asynchronous call) to changes in the forecast report for the current location when
 *    specific weather conditions occur
 * 2. SUBSCRIBE (asynchronous call) to changes in the forecast report for a specific location and
 *    date/time
 * 3. GET (synchronous call) the most recent forecast report for your current location
 * 4. GET (synchronous call) the most recent forecast report for a specific location
 * 5. UNSUBSCRIBE from receiving forecast updates for a specific location
 * 6. UNSUBSCRIBE from receiving forecast updates for the current location
 * 7. CHANGE weather condition rules and refresh times (for updates) in real time
 * 8. CHANGE forecast mode (hourly -> daily and vice versa)
 */
public class WeatherActivity extends ServicesActivity {
    private ViewHelper helper;
    private ListView weatherList;
    private ArrayAdapter<String> weatherAdapter;
    private TextView weatherInfo;
    String place1 = Constants.LOCATION_CURRENT_PLACE;
    String place2 = "Carnegie Mellon University";
    String place3 = "Denver";
    String place4 = "Paris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_weather_activity);
        weatherList = (ListView) findViewById(R.id.weatherView);
        weatherInfo = (TextView) findViewById(R.id.weatherInfo);
        weatherAdapter = new ArrayAdapter<>(this, R.layout.services_adapter_events, R.id.textListElement);
        weatherList.setAdapter(weatherAdapter);
        fm = getSupportFragmentManager();

        //controllers
        helper = ViewHelper.getInstance( this );
        MessageBroker.getInstance( getApplicationContext() ).subscribe( this );


        /** 1. SUBSCRIBE (asynchronous call) to changes in the forecast report for the current
         * location when specific weather conditions occur. If place is null or "" then Weather
         * Service will do nothing and if it is Constants.LOCATION_CURRENT_PLACE it will use the
         * current user's  location **/
        //weather condition rules: when any of these rules are fulfilled then the weather data is
        //updated and refreshed in the UI
        List<WeatherProposition> rules = new ArrayList<>();
        rules.add( new WeatherProposition( Constants.WEATHER_HIGH_TEMP_ENG,
                Constants.OPERATOR_HIGHER_THAN, "70", 2016, 9, 22 ));
        rules.add( new WeatherProposition( Constants.WEATHER_LOW_TEMP_ENG,
                Constants.OPERATOR_LOWER_THAN, "63", 2016, 9, 22 ));
        rules.add( new WeatherProposition( Constants.WEATHER_HUMIDITY,
                Constants.OPERATOR_HIGHER_THAN, "30", 2016, 9, 22, 18, 0 ));
        //it retrieves a forecast report once and checks every 60 min  whether weather condition rules
        //have changed. If no weather rules are provided then an update will be sent every 60 min
        //regardless if weather conditions remain equal or not. This is an event-oriented call
        //so the results of this request will be handled by onEvent methods below.
        helper.subscribeToWeatherReport(place1, 60 * 60 * 1000L, true, rules);

        /** 2. SUBSCRIBE (asynchronous call) to changes in the forecast report for a specific
         * location (Denver, Paris, etc...) and date **/
        List<WeatherProposition> rules2 = new ArrayList<>();
        rules2.add( new WeatherProposition( Constants.WEATHER_HIGH_TEMP_ENG,
                Constants.OPERATOR_HIGHER_THAN, "50", 2015, 9, 22 ));
        helper.subscribeToWeatherReport( place2, 60 * 60 * 1000L, true, rules2);
        helper.subscribeToWeatherReport( place3, 60 * 60 * 1000L, true, rules2);
        helper.subscribeToWeatherReport( place4, 60 * 60 * 1000L, true, rules2);

        //you can also add validation rules that triggers actions when the weather proposition is met
        helper.addRule();
    }


    /**
     * Changes the forecast mode (daily to hourly and vice versa) and shows the current weather data.
     * Unless the subscription mode, this method (getWeatherReport) get the more recent weather data
     * stored in the phone.
     * @param v
     */
    public void showWeather(View v) {
        /** 8. CHANGE forecast mode (hourly -> daily and vice versa) **/
        //you can either alternate between DAILY or HOURLY reports or directly specify which one you
        //need: Constants.WEATHER_FORECAST_DAILY, Constants.WEATHER_FORECAST_HOURLY
        int mode = helper.changeForecastMode( place1 );

        /** 3. GET (synchronous call) the most recent forecast report for your current location **/
        weatherInfo.setText( "Retrieving Weather Info for " + Constants.LOCATION_CURRENT_PLACE + ": " );
        weatherAdapter.clear();
        weatherAdapter.addAll( helper.getWeatherReport( Constants.LOCATION_CURRENT_PLACE, mode) );

        /** 4. GET (synchronous call) the most recent forecast report for a specific location **/
        weatherInfo.setText( "Retrieving Weather Info for Denver: " );
        weatherAdapter.clear();
        weatherAdapter.addAll( helper.getWeatherReport( "Denver", mode) );

        /** 7. CHANGE weather condition rules and refresh times (for weather updates) in real time **/
        List<WeatherProposition> rules = new ArrayList<>();
        rules.add( new WeatherProposition( Constants.WEATHER_HIGH_TEMP_ENG,
                Constants.OPERATOR_HIGHER_THAN, "60", 2015, 9, 22, 12, 30 ));
        // we can also modify the periodicity of the weather forecast updates: Now, Denver forecast
        // update will be every 15 seconds instead of every 45 seconds.
        helper.subscribeToWeatherReport( place3, 15 * 1000L, true, rules );
    }

    /***
     * EVENT HANDLER. Implement this method to process all the location updates
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent( final LocationEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( event.getErrorMessage() != null ){
                    Toast.makeText( WeatherActivity.this, event.getErrorMessage(),
                            Toast.LENGTH_LONG ).show();
                }
                //....
            }
        });
    }

    /***
     * EVENT HANDLER. Implement this method to process all the weather updates
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent( final WeatherEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( event.isError() ){
                    Toast.makeText( WeatherActivity.this, event.getErrorMessage(),
                            Toast.LENGTH_LONG ).show();
                }else{
                    weatherInfo.setText( "Retrieving Weather Info for " + event.getPlace() );
                    weatherAdapter.clear();
                    if( event.getForecastMode() == Constants.WEATHER_FORECAST_DAILY ){
                        weatherAdapter.addAll( event.getDailyWeatherString() );
                        //...
                    }else if( event.getForecastMode() == Constants.WEATHER_FORECAST_HOURLY ){
                        weatherAdapter.addAll( event.getHourlyWeatherString() );
                        //...
                    }else{
                        // show hourly report by default
                        weatherAdapter.addAll( event.getHourlyWeatherString() );
                        //...
                    }
                    weatherAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    /**
     * Here you can unsubscribe from weather updates and from MessageBroker
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        /** 5. UNSUBSCRIBE from receiving forecast updates for a specific location **/
        helper.unsubscribeToWeatherReport( place3 );
        /** 6. UNSUBSCRIBE from receiving forecast updates for your current location **/
        helper.unsubscribeToWeatherReport( Constants.LOCATION_CURRENT_PLACE );
        MessageBroker.getInstance( getApplicationContext() ).unsubscribe(this);
    }



    // ************************ AUTO-GENERATED ************************************************** //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
