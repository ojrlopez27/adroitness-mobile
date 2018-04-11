package edu.cmu.adroitness.client.sensors.battery.control;

import android.app.Activity;


import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;


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

    // ****************************** AWARE ***********************************************

    public boolean getStatusBattery() {
        return (Boolean) mMB.get( ViewHelper.this, MBRequest.build(Constants.MSG_STATUS_BATTERY));
    }


}
