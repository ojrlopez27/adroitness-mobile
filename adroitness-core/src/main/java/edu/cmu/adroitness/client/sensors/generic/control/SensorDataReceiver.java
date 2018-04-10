package edu.cmu.adroitness.client.sensors.generic.control;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.aware.Accelerometer;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.sensors.accelerometer.control.AccelerometerSensor;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.sensors.sms.control.SmsSensor;

/**
 * Created by oscarr on 8/13/15.
 */
public class SensorDataReceiver extends BroadcastReceiver {
    private MessageBroker mb;
    private Context mContext;
    private ResourceLocator resourceLocator;

    // This is required by the AndroidManifest
    public SensorDataReceiver(){}

    public SensorDataReceiver(Context context){
        mContext = context;
    }

    private void initialize( Context context ){
        if( mb == null ){
            mb = MessageBroker.getInstance( context );
        }
        if( resourceLocator == null ){
            resourceLocator = ResourceLocator.getInstance(context);
        }
    }

    //TODO: finish it
    @Override
    public void onReceive(Context context, final Intent intent) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                initialize(mContext);
                if (intent.getAction().equals(Accelerometer.ACTION_AWARE_ACCELEROMETER) ) {
                    ContentValues data = intent.getParcelableExtra(Accelerometer.EXTRA_DATA);
                    resourceLocator.lookupSensor(AccelerometerSensor.class).extractData(data);
                }else if( intent.getAction().equals( SmsSensor.SMS_RECEIVED ) ){
                    resourceLocator.lookupSensor(SmsSensor.class).processIncomingSMS( intent.getExtras() );
                }
            }
        });
    }
}
