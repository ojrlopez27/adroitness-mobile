
package edu.cmu.adroitness.client.services.location.control;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.aware.Aware_Preferences;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Locations_Provider.Locations_Data;
import com.aware.utils.Aware_Plugin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;
import edu.cmu.adroitness.client.services.location.view.LocationSettings;

/**
 * Fused location service for Aware framework
 * Requires Google Services API available on the device.
 * @author denzil
 */
public class Plugin extends Aware_Plugin implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * This plugin's package name
     */
    private final String PACKAGE_NAME = LocationService.class.getPackage().getName();
    
    //holds accuracy and frequency parameters
    private final static LocationRequest mLocationRequest = new LocationRequest();
    private static PendingIntent pIntent = null;
    public static GoogleApiClient mLocationClient = null;
    
    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "AWARE::Google Fused Location";
        DEBUG = AwareServiceWrapper.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");
        
        DATABASE_TABLES = Locations_Provider.DATABASE_TABLES;
        TABLES_FIELDS = Locations_Provider.TABLES_FIELDS;
        CONTEXT_URIS = new Uri[]{ Locations_Data.CONTENT_URI };
        
        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
//            MessageBroker.getInstance( getApplicationContext() ).send( LocationService.fillEvent( new LocationEvent() ));
            }
        };
        
        AwareServiceWrapper.setSetting(this, LocationSettings.STATUS_GOOGLE_FUSED_LOCATION, true);
        if( AwareServiceWrapper.getSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION).length() == 0 ) {
            AwareServiceWrapper.setSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION, LocationSettings.update_interval);
        } else {
            AwareServiceWrapper.setSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION, AwareServiceWrapper.getSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION));
        }
        
        if( AwareServiceWrapper.getSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION).length() == 0) {
            AwareServiceWrapper.setSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION, LocationSettings.max_update_interval);
        } else {
            AwareServiceWrapper.setSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION, AwareServiceWrapper.getSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION));
        }
        
        if( AwareServiceWrapper.getSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION).length() == 0) {
            AwareServiceWrapper.setSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, LocationSettings.location_accuracy);
        } else {
            AwareServiceWrapper.setSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, AwareServiceWrapper.getSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION));
        }
        
        mLocationRequest.setPriority(Integer.parseInt(AwareServiceWrapper.getSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION)));
        mLocationRequest.setInterval(Long.parseLong(AwareServiceWrapper.getSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION)) * 1000);
        mLocationRequest.setFastestInterval(Long.parseLong(AwareServiceWrapper.getSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION)) * 1000);

        if( !UtilServiceAPIs.isGooglePlayServicesAvailable(getApplicationContext() ) ) {
            Log.e(TAG,"Google Services fused location is not available on this device.");
            stopSelf();
        } else {
            Intent locationIntent = new Intent(this, LocationService.class);
            pIntent = PendingIntent.getService(this, 0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mLocationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //LocationService service = ResourceLocator.getInstance(getApplicationContext()).lookupService(LocationService.class);
       // if( service != null ) {
            ResourceLocator.getInstance(getApplicationContext()).addRequest(LocationService.class,
                    "getCurrentLocationByGoogleFused");
       // }
        mLocationClient.connect();
        if( mLocationClient.isConnected() ) {
            mLocationRequest.setPriority(Integer.parseInt(AwareServiceWrapper
                    .getSetting(Plugin.this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION)));
            mLocationRequest.setInterval(Long.parseLong(AwareServiceWrapper.getSetting(
                    Plugin.this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION)) * 1000);
            mLocationRequest.setFastestInterval(Long.parseLong(AwareServiceWrapper
                    .getSetting(Plugin.this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION)) * 1000);
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,
                        mLocationRequest, pIntent);
            }catch(SecurityException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        AwareServiceWrapper.setSetting(this, LocationSettings.STATUS_GOOGLE_FUSED_LOCATION, false);

        if( mLocationClient != null && mLocationClient.isConnected() ) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, pIntent);
        }
        AwareServiceWrapper.stopPlugin(this, PACKAGE_NAME);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connection_result ) {
        if( DEBUG ) Log.w(TAG, "Error connecting to Google Fused Location services, will try again in 5 minutes");
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.d(TAG,"Connected to Google's Location API");
        AwareServiceWrapper.startPlugin(this, PACKAGE_NAME);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if( DEBUG ) Log.w(TAG,"Error connecting to Google Fused Location services, will try again in 5 minutes");
    }

    public GoogleApiClient getmLocationClient() {
        return mLocationClient;
    }
}
