package edu.cmu.adroitness.client.sensors.accelerometer.control;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.aware.Accelerometer;
import com.aware.Aware_Preferences;
import com.aware.providers.Accelerometer_Provider;
import edu.cmu.adroitness.comm.accelerometer.model.AccelerometerEvent;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.sensors.generic.control.SensorObserver;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;

public class AccelerometerSensor extends SensorObserver {
    private Float accelerationX;
    private Float accelerationY;
    private Float accelerationZ;
    private Integer accuracy;
    private Long timestamp;

    public AccelerometerSensor(Handler handler, Context context) {
        super(handler, context);
        this.actions.add(Accelerometer.ACTION_AWARE_ACCELEROMETER);
        mUri = Accelerometer_Provider.Accelerometer_Data.CONTENT_URI;
        name = Aware_Preferences.STATUS_ACCELEROMETER;
    }

    public void unregister(BroadcastReceiver receiver){
        super.unregister(receiver);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d("onChange", "onIt");
        // Get the latest recorded value
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Cursor data = mContext.getContentResolver().query(mUri, new String[]{
                        Accelerometer_Provider.Accelerometer_Data.VALUES_0,
                        Accelerometer_Provider.Accelerometer_Data.VALUES_1,
                        Accelerometer_Provider.Accelerometer_Data.VALUES_2,
                        Accelerometer_Provider.Accelerometer_Data.ACCURACY,
                        Accelerometer_Provider.Accelerometer_Data.TIMESTAMP
                }, null, null, null);
                extractData(data);
            }
        });
    }


    public void extractData(Cursor data){
        if (data != null && data.moveToLast()) {
            // Here we read the value
            accelerationX = data.getFloat(data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
            accelerationY = data.getFloat(data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
            accelerationZ = data.getFloat(data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));
            accuracy = data.getInt(data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.ACCURACY));
            timestamp = data.getLong(data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.TIMESTAMP));
            notifySubscribers();
        }
        data.close();
        //mContext.getContentResolver().delete(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, null, null);
    }

    public void extractData(ContentValues data){
        if (data != null && enable) {
            // Here we read the value
            accelerationX = (Float) data.get("double_values_0");
            accelerationY = (Float) data.get("double_values_1");
            accelerationZ = (Float) data.get("double_values_2");
            accuracy = (Integer) data.get("accuracy");
            timestamp = (Long) data.get("timestamp");
            notifySubscribers();
        }
    }

    private void notifySubscribers(){
        mb.send(AccelerometerSensor.this,
                AccelerometerEvent.build().setAccelerationX(accelerationX).setAccelerationY(accelerationY)
                .setAccelerationZ(accelerationZ).setAccuracy(accuracy).setTimestamp(timestamp));
    }


    public Long getFrequency() {
        return Long.getLong(AwareServiceWrapper.getSetting(mContext,
                Aware_Preferences.FREQUENCY_ACCELEROMETER), -1); //200000L
    }

    public void startListening(){
        super.startListening();
        setFrequency( 200000L );
    }

    /**
     * Non-deterministic frequency in microseconds (dependent of the hardware sensor capabilities
     * and resources), e.g., 200000 (normal), 60000 (UI), 20000 (game), 0 (fastest).
     * @param frequency
     */
    public void setFrequency(Long frequency) {
        AwareServiceWrapper.setSetting( mContext, Aware_Preferences.FREQUENCY_ACCELEROMETER, frequency );
    }

    public AccelerometerEvent getAccEvent() {
        return AccelerometerEvent.build()
                .setAccelerationX( accelerationX )
                .setAccelerationY( accelerationY )
                .setAccelerationZ( accelerationZ )
                .setAccuracy( accuracy )
                .setTimestamp( timestamp );
    }
}