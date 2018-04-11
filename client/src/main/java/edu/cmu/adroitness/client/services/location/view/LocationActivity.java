package edu.cmu.adroitness.client.services.location.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.location.control.ViewHelper;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.location.model.LocationEvent;

/**
 * You will be able to:
 * 1. SUBSCRIBE to updates for current location changes (e.g., when user moves or changes his location)
 * 2. GET the more recent Current Location data (it uses GPS, WiFi Networks, Cache Data and Yahoo Woeid)
 * 3. GET Location Data for a different place (it uses GPS, WiFi Networks, Cache Data and Yahoo Woeid)
 * 4. GET the history of location points (in the case that this is enabled)
 */
public class LocationActivity extends AppCompatActivity {

    private TextView placeName;
    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private TextView speed;
    private TextView accuracy;
    private TextView address;
    private TextView country;
    private TextView state;
    private TextView city;
    private TextView town;
    private TextView subArea;
    private TextView zipCode;
    private TextView timeZone;
    private TextView woeid;
    private boolean isCurrentLocation = true;
    private String place;

    //controls
    private ViewHelper helper;
    private MessageBroker mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_location_activity);
        placeName = (TextView) findViewById( R.id.locationPlace);
        latitude = (TextView) findViewById( R.id.locationLatitude);
        longitude = (TextView) findViewById( R.id.locationLongitude);
        altitude = (TextView) findViewById( R.id.locationAltitude);
        speed = (TextView) findViewById( R.id.locationSpeed);
        accuracy = (TextView) findViewById( R.id.locationAccuracy);
        address = (TextView) findViewById( R.id.locationAddress);
        country = (TextView) findViewById( R.id.locationCountry);
        state = (TextView) findViewById( R.id.locationState);
        city = (TextView) findViewById( R.id.locationCity);
        town = (TextView) findViewById( R.id.locationTown);
        subArea = (TextView) findViewById( R.id.locationSubArea);
        zipCode = (TextView) findViewById( R.id.locationZipCode);
        timeZone = (TextView) findViewById( R.id.locationTimeZone);
        woeid = (TextView) findViewById( R.id.locationWoeid);

        //controllers
        helper = ViewHelper.getInstance( this );
        mb = MessageBroker.getInstance( getApplicationContext() );

        /** 1. SUBSCRIBE to updates for current location changes (e.g., when user moves or changes
         * his/her location). Results are processed by onEvent method and it is required
         * user's phone has the Location mode enabled (GPS + WiFi Networks) **/
        mb.subscribe( this );

        // The procedure to get the current location is as follows:
        // 1. If Location Mode has enabled both GPS and WiFi Networks, then use them to pinpoint the
        //    location with more precision
        // 2. Otherwise, if any of these two (GPS or WiFi Network) is enabled, then use it
        // 3. Otherwise, if Location Mode is disabled and setWriteReadLocationFromFile is set to true
        //    then it loads the last location that was stored in the phone
        // 4. Otherwise, if Location Mode is disabled and defaultCurrentPlace is set, then it uses
        //    this place to get the location data
        // 5. Otherwise, it shows a popup notification to the user, saying that Location Mode is off
        helper.setWriteReadLocationFromFile(new Boolean(true));

        // In the case Location mode is disabled in the user's phone (GPS, WiFi Network location, etc.)
        // you still can get data about the current location (e.g., latitude, longitude, etc.) by
        // indicating a default place, otherwise, you will get an error message requiring to enable
        // Location mode in user's phone
        helper.setDefaultCurrentPlace("820 Elizabeth Street, Turtle Creek");
        // OR you can provide coordinates:
        //helper.setDefaultCurrentCoordinates( 40.3285, -79.2034 );
    }


    /**
     * 2. GET the more recent Current Location data (it uses GPS, WiFi Networks, Cache Data and
     * Yahoo Woeid)
     * @param view
     */
    public void showCurrentLocation(View view){
        isCurrentLocation = true;
        fillData( helper.getCurrentLocation() );
    }


    /**
     * 3. GET Location Data for a different place, e.g., Paris (it uses GPS, WiFi Networks, Cache
     * Data and Yahoo Woeid)
     * @param view
     */
    public void showOtherLocation(View view){
        isCurrentLocation = false;
        place = "Paris,France";
        fillData( helper.getLocation(place) );
    }


    public void getHistoryLocations(){
        // history record can be switched on or off. You should do it at the onCreate method,
        helper.enableHistoryRecord( new Boolean(true) );

        // now, get the history:
        ArrayList<LocationEvent> history = helper.getHistoryLocations();
        for( LocationEvent locationEvent : history ){
            Log.d("LocationActivity", "Location: " + locationEvent.getPlaceName() + " time: "
                    + Util.formatDate( locationEvent.getTimestamp() ) );
        }

        // and you can reset (clear) the history by doing that:
        helper.resetHistoryLocations();

        // you can specify a maximum number of location objects in the history (500 by default)
        helper.setMaxNumHistoryLocations( new Integer(1000) );
    }

    /**
     * In the case user has Location Mode disabled (GPS and WiFi networks off) then the last
     * current location is cached, so you may want to reset this data by doing (otherwise don't
     * use it)
     */
    public void reset(){
        helper.resetCurrentLocation();
    }


    /**
     * THIS IS THE EVENT HANDLER. IMPLEMENT THIS TO CATCH AND PROCESS ALL CURRENT LOCATION
     * ASYNCHRONOUS CALLS
     * @param locationEvent
     */
    public void onEvent( final LocationEvent locationEvent ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( isCurrentLocation ) {
                    if (locationEvent.getErrorMessage() != null) {
                        Toast.makeText(LocationActivity.this, locationEvent.getErrorMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        fillData(locationEvent);
                    }
                }
            }
        });
    }

    private void fillData( LocationEvent locationEvent){
        placeName.setText( locationEvent.getPlaceName() );
        latitude.setText( "" + locationEvent.getLatidude() );
        longitude.setText( "" + locationEvent.getLongitude() );
        altitude.setText( "" + locationEvent.getAltitude() );
        speed.setText( "" + locationEvent.getSpeed() );
        accuracy.setText( "" + locationEvent.getAccuracy() );
        address.setText( locationEvent.getAddress() );
        country.setText( locationEvent.getCountry() + ", " + locationEvent.getCountryCode() );
        state.setText( locationEvent.getState() + ", " + locationEvent.getStateCode() );
        city.setText( locationEvent.getCity() );
        town.setText( locationEvent.getTown() );
        subArea.setText( locationEvent.getSubArea() );
        zipCode.setText( locationEvent.getZipcode() );
        timeZone.setText( locationEvent.getTimezone() );
        woeid.setText( locationEvent.getWoeid() );
    }





    // ************************ AUTO-GENERATED ************************************************** //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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
