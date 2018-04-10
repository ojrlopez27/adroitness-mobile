package edu.cmu.adroitness.client.sensors.phonecall.control;

import android.content.Context;
import android.os.Handler;

import edu.cmu.adroitness.client.sensors.generic.control.SensorObserver;

/**
 * Created by oscarr on 9/29/15.
 */
public class PhoneCallSensor extends SensorObserver{

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     * @param context
     */
    public PhoneCallSensor(Handler handler, Context context) {
        super(handler, context);
    }

    @Override
    public void startListening() {

    }

    @Override
    public void stopListening() {

    }
}
