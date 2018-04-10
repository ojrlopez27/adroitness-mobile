
package edu.cmu.adroitness.client.services.location.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.location.LocationRequest;
import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;


public class LocationSettings extends Activity {

	/**
	 * Boolean to activate/deactivate Google Fused Location
	 */
    public static final String STATUS_GOOGLE_FUSED_LOCATION = "status_google_fused_location";
    /**
     * How frequently should we try to acquire location (in seconds)
     */
    public static final String FREQUENCY_GOOGLE_FUSED_LOCATION = "frequency_google_fused_location";
    /**
     * How fast you are willing to get the latest location (in seconds)
     */
    public static final String MAX_FREQUENCY_GOOGLE_FUSED_LOCATION = "max_frequency_google_fused_location";
    /**
     * How important is accuracy to you and battery impact. One of the following:<br/>
     * {@link LocationRequest#PRIORITY_HIGH_ACCURACY}<br/>
     * {@link LocationRequest#PRIORITY_BALANCED_POWER_ACCURACY}<br/>
     * {@link LocationRequest#PRIORITY_LOW_POWER}<br/>
     * {@link LocationRequest#PRIORITY_NO_POWER}
     */
    public static final String ACCURACY_GOOGLE_FUSED_LOCATION = "accuracy_google_fused_location";
    
    /**
     * Update interval for location, in seconds ( default = 180 )
     */
    public static int update_interval = 180;
    
    /**
     * Fastest update interval for location, in seconds ( default = 1 )
     */
    public static int max_update_interval = 1;
    
    /**
     * Desired location accuracy (default = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY <br/>
     * Other possible Values: 
     * {@link LocationRequest#PRIORITY_HIGH_ACCURACY} <br/>
     * {@link LocationRequest#PRIORITY_BALANCED_POWER_ACCURACY} <br/>
     * {@link LocationRequest#PRIORITY_LOW_POWER} <br/>
     * {@link LocationRequest#PRIORITY_NO_POWER}
     */
    public static int location_accuracy = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    
    private static EditText update_frequency = null;
    private static EditText max_update_frequency = null;
    private static Spinner accuracy_level = null;
    private static ArrayAdapter<CharSequence> adapter = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.location_settings);

        update_frequency = (EditText) findViewById(R.id.update_frequency);
        update_frequency.setText(AwareServiceWrapper.getSetting(this, LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION));
        
        max_update_frequency = (EditText) findViewById(R.id.fastest_update_frequency);
        max_update_frequency.setText(AwareServiceWrapper.getSetting(this, LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION));
        
        accuracy_level = (Spinner) findViewById(R.id.accuracy_level);
        adapter = ArrayAdapter.createFromResource(this, R.array.accuracies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accuracy_level.setAdapter(adapter);
        
        switch(Integer.parseInt(AwareServiceWrapper.getSetting(this, LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION))) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                accuracy_level.setSelection(0);
                location_accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY;
                break;
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                accuracy_level.setSelection(1);
                location_accuracy = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                break;
            case LocationRequest.PRIORITY_LOW_POWER:
                accuracy_level.setSelection(2);
                location_accuracy = LocationRequest.PRIORITY_LOW_POWER;
                break;
            case LocationRequest.PRIORITY_NO_POWER:
                accuracy_level.setSelection(3);
                location_accuracy = LocationRequest.PRIORITY_NO_POWER;
                break;
            default:
                accuracy_level.setSelection(1);
                location_accuracy = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                break;
        }
        
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( update_frequency.getText().length() > 0 ) {
                    AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION, update_frequency.getText().toString());
                }
                if( max_update_frequency.getText().length() > 0) {
                    AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION, max_update_frequency.getText().toString());
                }
                switch( accuracy_level.getSelectedItemPosition() ) {
                    case 0:
                        AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, LocationRequest.PRIORITY_HIGH_ACCURACY);
                        break;
                    case 1:
                        AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        break;
                    case 2:
                        AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, LocationRequest.PRIORITY_LOW_POWER);
                        break;
                    case 3:
                    	AwareServiceWrapper.setSetting(getApplicationContext(), LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION, LocationRequest.PRIORITY_NO_POWER);
                        break;
                }
                


                finish();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        update_frequency.setText(AwareServiceWrapper.getSetting(getApplicationContext(), LocationSettings.FREQUENCY_GOOGLE_FUSED_LOCATION));
        max_update_frequency.setText(AwareServiceWrapper.getSetting(getApplicationContext(), LocationSettings.MAX_FREQUENCY_GOOGLE_FUSED_LOCATION));
        
        switch(Integer.parseInt(AwareServiceWrapper.getSetting(getApplicationContext(), LocationSettings.ACCURACY_GOOGLE_FUSED_LOCATION))) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                accuracy_level.setSelection(0);
                location_accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY;
                break;
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                accuracy_level.setSelection(1);
                location_accuracy = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                break;
            case LocationRequest.PRIORITY_LOW_POWER:
                accuracy_level.setSelection(2);
                location_accuracy = LocationRequest.PRIORITY_LOW_POWER;
                break;
            case LocationRequest.PRIORITY_NO_POWER:
                accuracy_level.setSelection(3);
                location_accuracy = LocationRequest.PRIORITY_NO_POWER;
                break;
            default:
                accuracy_level.setSelection(1);
                location_accuracy = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                break;
        }
    }
}
