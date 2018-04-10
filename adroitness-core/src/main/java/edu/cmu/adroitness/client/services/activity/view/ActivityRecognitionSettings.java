package edu.cmu.adroitness.client.services.activity.view;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;

public class ActivityRecognitionSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	/**
	 * State of Google's Activity Recognition plugin
	 */
	public static final String STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION = "status_plugin_google_activity_recognition";
	
	/**
	 * Frequency of Google's Activity Recognition plugin in seconds<br/>
	 * By default = 60 seconds
	 */
	public static final String FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION = "frequency_plugin_google_activity_recognition";

	private static CheckBoxPreference status;
	private static EditTextPreference frequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION);
		if( AwareServiceWrapper.getSetting(getApplicationContext(), STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION).length() == 0 ) {
			AwareServiceWrapper.setSetting(this, STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, true);
        }
		status.setChecked(AwareServiceWrapper.getSetting(getApplicationContext(), STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION).equals("true"));
		frequency = (EditTextPreference) findPreference(FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION);
        if( AwareServiceWrapper.getSetting(this, FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION).length() == 0 ) {
            AwareServiceWrapper.setSetting(this, FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, 60);
        }
		frequency.setSummary(AwareServiceWrapper.getSetting(getApplicationContext(), FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION) + " seconds");
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference preference = findPreference(key);

		if( preference.getKey().equals(STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION) ) {
			boolean is_active = sharedPreferences.getBoolean(key, false);
			AwareServiceWrapper.setSetting( getApplicationContext(), key, is_active);
            if( is_active ) {
                AwareServiceWrapper.startPlugin(getApplicationContext(), getPackageName());
            } else {
                AwareServiceWrapper.stopPlugin(getApplicationContext(), getPackageName());
            }
            status.setChecked(is_active);
		}
		if( preference.getKey().equals(FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION)) {
            AwareServiceWrapper.setSetting( getApplicationContext(), key, sharedPreferences.getString(key, "60") );
            frequency.setSummary(AwareServiceWrapper.getSetting(getApplicationContext(), FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION) + " seconds");
            AwareServiceWrapper.startPlugin(getApplicationContext(), getPackageName());
		}

	}	
}
