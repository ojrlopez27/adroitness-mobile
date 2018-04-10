
package edu.cmu.adroitness.client.services.activity.control;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.aware.Aware_Preferences;
import com.aware.utils.Aware_Plugin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import edu.cmu.adroitness.comm.ar.model.ActivityRecognitionEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.activity.view.ActivityRecognitionSettings;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;


public class Plugin extends Aware_Plugin implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private String PACKAGE_NAME = Plugin.class.getPackage().getName();

	private static Intent gARIntent;
	private static PendingIntent gARPending;
	private static GoogleApiClient gARClient;

	public static int activityType = -1;
	public static int confidence = -1;

	@Override
	public void onCreate() {
		super.onCreate();

		TAG = "AWARE::Google Activity Recognition";

		DATABASE_TABLES = Provider.DATABASE_TABLES;
		TABLES_FIELDS = Provider.TABLES_FIELDS;
		CONTEXT_URIS = new Uri[]{Provider.Google_Activity_Recognition_Data.CONTENT_URI };
		
		CONTEXT_PRODUCER = new ContextProducer() {
			@Override
			public void onContext() {
				ActivityRecognitionEvent event = new ActivityRecognitionEvent();
				event.setActivityType(Plugin.activityType);
				event.setConfidence(Plugin.confidence);
				MessageBroker.getInstance(getApplicationContext()).send(Plugin.this, event);
			}
		};

		AwareServiceWrapper.setSetting(getApplicationContext(),
				ActivityRecognitionSettings.STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, true);
		if( AwareServiceWrapper.getSetting(getApplicationContext(),
                ActivityRecognitionSettings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION).length() == 0 ) {
			AwareServiceWrapper.setSetting(getApplicationContext(),
                    ActivityRecognitionSettings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, 60);
		}

		gARIntent = new Intent(this, ActivityRecognitionService.class);
		gARPending = PendingIntent.getService(getApplicationContext(), 0, gARIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        gARClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

		AwareServiceWrapper.setSetting(this,
                ActivityRecognitionSettings.STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, true);
		if( AwareServiceWrapper.getSetting(this,
                ActivityRecognitionSettings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION).length() == 0 ) {
			AwareServiceWrapper.setSetting(this,
                    ActivityRecognitionSettings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, 60);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DEBUG = AwareServiceWrapper.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

		if ( !UtilServiceAPIs.isGooglePlayServicesAvailable(getApplicationContext())) {
			Log.e(TAG,"Google Services activity recognition not available on this device.");
			stopSelf();
		} else {
			gARClient.connect();
            if( gARClient.isConnected() ) {
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(gARClient,
                        Long.valueOf(AwareServiceWrapper.getSetting(getApplicationContext(),
                                ActivityRecognitionSettings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION))
                                * 1000, gARPending);
            }
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        AwareServiceWrapper.setSetting(getApplicationContext(),
                ActivityRecognitionSettings.STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, false);

		//we might get here if phone doesn't support Google Services
		if ( gARClient != null && gARClient.isConnected() ) {
			ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates( gARClient, gARPending );
		}
        AwareServiceWrapper.stopPlugin(this, PACKAGE_NAME);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connection_result) {
		Log.w(TAG,"Error connecting to Google's activity recognition services, will try again in 5 minutes");
	}

	@Override
	public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to Google's Activity Recognition API");
		AwareServiceWrapper.startPlugin(this, PACKAGE_NAME);
	}

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG,"Error connecting to Google's activity recognition services, will try again in 5 minutes");
    }
}
