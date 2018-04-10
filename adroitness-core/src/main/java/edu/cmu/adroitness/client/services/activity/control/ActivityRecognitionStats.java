package edu.cmu.adroitness.client.services.activity.control;

import android.content.ContentResolver;
import android.database.Cursor;

import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionStats {
	
	/**
	 * Get the total amount of time still between two timestamps
	 * @param resolver
	 * @param timestamp_start
	 * @param timestamp_end
	 * @return total_time_still
	 */
	public static long getTimeStill(ContentResolver resolver, long timestamp_start, long timestamp_end) {
		long total_time_still = 0;
		
		String selection = Provider.Google_Activity_Recognition_Data.TIMESTAMP + " between " + timestamp_start + " AND " + timestamp_end;
        Cursor activity_raw = resolver.query(Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, selection, null, Provider.Google_Activity_Recognition_Data.TIMESTAMP + " ASC");
		if( activity_raw != null && activity_raw.moveToFirst() ) {
			int last_activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
			long last_activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
			
			while(activity_raw.moveToNext()) {
				int activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
				long activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
				
				if( activity == DetectedActivity.STILL && last_activity == DetectedActivity.STILL ) { //continuing still
					total_time_still += activity_timestamp-last_activity_timestamp;
				}
				
				last_activity = activity;
				last_activity_timestamp = activity_timestamp;
			}
		}
		if( activity_raw != null && ! activity_raw.isClosed() ) activity_raw.close();
		return total_time_still;
	}
	
	/**
	 * Get the total amount of time biking between two timestamps
	 * @param resolver
	 * @param timestamp_start
	 * @param timestamp_end
	 * @return total_time_still
	 */
	public static long getTimeBiking(ContentResolver resolver, long timestamp_start, long timestamp_end) {
		long total_time_bike = 0;
		
		String selection = Provider.Google_Activity_Recognition_Data.TIMESTAMP + " between " + timestamp_start + " AND " + timestamp_end;
        Cursor activity_raw = resolver.query(Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, selection, null, Provider.Google_Activity_Recognition_Data.TIMESTAMP + " ASC");
		if( activity_raw != null && activity_raw.moveToFirst() ) {
			int last_activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
			long last_activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
			
			while(activity_raw.moveToNext()) {
				int activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
				long activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
				
				if( activity == DetectedActivity.ON_BICYCLE && last_activity == DetectedActivity.ON_BICYCLE ) { //continuing biking 
                    total_time_bike += activity_timestamp-last_activity_timestamp;
				}
				
				last_activity = activity;
				last_activity_timestamp = activity_timestamp;
			}
		}
		if( activity_raw != null && ! activity_raw.isClosed() ) activity_raw.close();
		return total_time_bike;
	}
	
	/**
	 * Get the total amount of time still between two timestamps
	 * @param resolver
	 * @param timestamp_start
	 * @param timestamp_end
	 * @return total_time_still
	 */
	public static long getTimeVehicle(ContentResolver resolver, long timestamp_start, long timestamp_end) {
		long total_time_vehicle = 0;
		
		String selection = Provider.Google_Activity_Recognition_Data.TIMESTAMP + " between " + timestamp_start + " AND " + timestamp_end;
        Cursor activity_raw = resolver.query(Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, selection, null, Provider.Google_Activity_Recognition_Data.TIMESTAMP + " ASC");
		if( activity_raw != null && activity_raw.moveToFirst() ) {
			int last_activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
			long last_activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
			
			while(activity_raw.moveToNext()) {
				int activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
				long activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
				
				if( activity == DetectedActivity.IN_VEHICLE && last_activity == DetectedActivity.IN_VEHICLE ) { //continuing in vehicle 
                    total_time_vehicle += activity_timestamp-last_activity_timestamp;
				}
				
				last_activity = activity;
				last_activity_timestamp = activity_timestamp;
			}
		}
		if( activity_raw != null && ! activity_raw.isClosed() ) activity_raw.close();
		return total_time_vehicle;
	}
	
	/**
	 * Get the total amount of time still between two timestamps
	 * @param resolver
	 * @param timestamp_start
	 * @param timestamp_end
	 * @return total_time_still
	 */
	public static long getTimeWalking(ContentResolver resolver, long timestamp_start, long timestamp_end) {
		long total_time_walking = 0;
		
		String selection = Provider.Google_Activity_Recognition_Data.TIMESTAMP + " between " + timestamp_start + " AND " + timestamp_end;
        Cursor activity_raw = resolver.query(Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, selection, null, Provider.Google_Activity_Recognition_Data.TIMESTAMP + " ASC");
		if( activity_raw != null && activity_raw.moveToFirst() ) {

            int last_activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
			long last_activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
			
			while(activity_raw.moveToNext()) {
				int activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
				long activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));
				
				if( activity == DetectedActivity.WALKING && last_activity == DetectedActivity.WALKING ) { //continuing walking
                    total_time_walking += activity_timestamp-last_activity_timestamp;
				}
				
				last_activity = activity;
				last_activity_timestamp = activity_timestamp;
			}
		}
		if( activity_raw != null && ! activity_raw.isClosed() ) activity_raw.close();
		return total_time_walking;
	}

	/**
	 * Get the total amount of time running between two timestamps
	 * @param resolver
	 * @param timestamp_start
	 * @param timestamp_end
	 * @return total_time_still
	 */
	public static long getTimeRunning(ContentResolver resolver, long timestamp_start, long timestamp_end) {
		long total_time_running = 0;

		String selection = Provider.Google_Activity_Recognition_Data.TIMESTAMP + " between " + timestamp_start + " AND " + timestamp_end;
		Cursor activity_raw = resolver.query(Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, selection, null, Provider.Google_Activity_Recognition_Data.TIMESTAMP + " ASC");
		if( activity_raw != null && activity_raw.moveToFirst() ) {

			int last_activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
			long last_activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));

			while(activity_raw.moveToNext()) {
				int activity = activity_raw.getInt(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE));
				long activity_timestamp = activity_raw.getLong(activity_raw.getColumnIndex(Provider.Google_Activity_Recognition_Data.TIMESTAMP));

				if( activity == DetectedActivity.RUNNING && last_activity == DetectedActivity.RUNNING ) { //continuing running
					total_time_running += activity_timestamp-last_activity_timestamp;
				}

				last_activity = activity;
				last_activity_timestamp = activity_timestamp;
			}
		}
		if( activity_raw != null && ! activity_raw.isClosed() ) activity_raw.close();
		return total_time_running;
	}
}
