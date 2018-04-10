package edu.cmu.adroitness.client.sensors.accelaration.control;

import android.app.Activity;

import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.generic.model.MBRequest;
import com.yahoo.inmind.commons.control.Constants;

/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private Activity mActivity;


    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    // ****************************** ACCELEROMETER ***********************************************

    public void stopAccelerometer() {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SENSOR_SETTINGS)
                .put(Constants.SENSOR_NAME, Constants.SENSOR_ACCELEROMETER)
                .put(Constants.SENSOR_SETTING, Constants.SENSOR_STOP));
    }

    public void startAccelerometer() {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SENSOR_SETTINGS)
                .put(Constants.SENSOR_NAME, Constants.SENSOR_ACCELEROMETER)
                .put(Constants.SENSOR_SETTING, Constants.SENSOR_START)
                .put(Constants.ACCELEROMETER_FREQUENCY, 200000L )); //in microseconds
    }
}
