/**
@author: denzil
 */

package edu.cmu.adroitness.client.services.activity.control;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import com.aware.Aware;
import com.aware.utils.DatabaseHelper;

import java.util.HashMap;

/**
 * Google's Activity Recognition Context Provider. Stored in SDcard in
 * AWARE/plugin_google_activity_recognition.db
 * 
 * @author denzil
 */
public class Provider extends ContentProvider {

    public static final int DATABASE_VERSION = 5;

    /**
     * Provider authority: com.aware.provider.plugin.google.activity_recognition
     */
    public static String AUTHORITY;

    private static final int GOOGLE_AR = 1;
    private static final int GOOGLE_AR_ID = 2;

    public static final class Google_Activity_Recognition_Data implements BaseColumns {
        private Google_Activity_Recognition_Data() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://"+ Provider.AUTHORITY + "/plugin_google_activity_recognition");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aware.plugin.google.activity_recognition";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aware.plugin.google.activity_recognition";

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";
        public static final String ACTIVITY_NAME = "activity_name";
        public static final String ACTIVITY_TYPE = "activity_type";
        public static final String CONFIDENCE = "confidence";
        /**
         * JSONArray representation of all the activities and confidence values<br/>
         * [{'activity':'walking','confidence':90},...]
         */
        public static final String ACTIVITIES = "activities";
    }

    public static String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/AWARE/" + "plugin_google_activity_recognition.db";

    public static final String[] DATABASE_TABLES = {
            "plugin_google_activity_recognition"
    };

    public static final String[] TABLES_FIELDS = {
            Google_Activity_Recognition_Data._ID + " integer primary key autoincrement," + 
            Google_Activity_Recognition_Data.TIMESTAMP + " real default 0," + 
            Google_Activity_Recognition_Data.DEVICE_ID + " text default ''," +
            Google_Activity_Recognition_Data.ACTIVITY_NAME + " text default ''," +
            Google_Activity_Recognition_Data.ACTIVITY_TYPE + " integer default 0," +
            Google_Activity_Recognition_Data.CONFIDENCE + " integer default 0," +
            Google_Activity_Recognition_Data.ACTIVITIES + " text default ''," +
            "UNIQUE (" + Google_Activity_Recognition_Data.TIMESTAMP + "," + Google_Activity_Recognition_Data.DEVICE_ID + ")"
    };

    private static UriMatcher sUriMatcher = null;
    private static HashMap<String, String> gARMap = null;
    private static DatabaseHelper databaseHelper = null;
    private static SQLiteDatabase database = null;

    private boolean initializeDB() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            database = databaseHelper.getWritableDatabase();
        }
        return( database != null && databaseHelper != null);
    }
    
    /**
     * Delete entry from the database
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case GOOGLE_AR:
                count = database.delete(DATABASE_TABLES[0], selection,
                        selectionArgs);
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case GOOGLE_AR:
                return Google_Activity_Recognition_Data.CONTENT_TYPE;
            case GOOGLE_AR_ID:
                return Google_Activity_Recognition_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * Insert entry to the database
     */
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
    	if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case GOOGLE_AR:
                long google_AR_id = database.insert(DATABASE_TABLES[0],
                        Google_Activity_Recognition_Data.ACTIVITY_NAME, values);

                if (google_AR_id > 0) {
                    Uri new_uri = ContentUris.withAppendedId(
                            Google_Activity_Recognition_Data.CONTENT_URI,
                            google_AR_id);
                    getContext().getContentResolver().notifyChange(new_uri,
                            null);
                    return new_uri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
    	AUTHORITY = getContext().getPackageName() + ".provider.activity";
    	
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.AUTHORITY, DATABASE_TABLES[0],
                GOOGLE_AR);
        sUriMatcher.addURI(Provider.AUTHORITY, DATABASE_TABLES[0]
                + "/#", GOOGLE_AR_ID);

        gARMap = new HashMap<String, String>();
        gARMap.put(Google_Activity_Recognition_Data._ID,
                Google_Activity_Recognition_Data._ID);
        gARMap.put(Google_Activity_Recognition_Data.TIMESTAMP,
                Google_Activity_Recognition_Data.TIMESTAMP);
        gARMap.put(Google_Activity_Recognition_Data.DEVICE_ID,
                Google_Activity_Recognition_Data.DEVICE_ID);
        gARMap.put(Google_Activity_Recognition_Data.ACTIVITY_NAME,
                Google_Activity_Recognition_Data.ACTIVITY_NAME);
        gARMap.put(Google_Activity_Recognition_Data.ACTIVITY_TYPE,
                Google_Activity_Recognition_Data.ACTIVITY_TYPE);
        gARMap.put(Google_Activity_Recognition_Data.CONFIDENCE,
                Google_Activity_Recognition_Data.CONFIDENCE);
        gARMap.put(Google_Activity_Recognition_Data.ACTIVITIES,
                Google_Activity_Recognition_Data.ACTIVITIES);
    	
        return true;
    }

    /**
     * Query entries from the database
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        
    	if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case GOOGLE_AR:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(gARMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG)
                Log.e(Aware.TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Update application on the database
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        
    	if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }
    	
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case GOOGLE_AR:
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
