package edu.cmu.adroitness.client.sensors.sms.control;

import edu.cmu.adroitness.client.sensors.sms.view.SmsSensorActivity;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.generic.model.MBRequest;
import com.yahoo.inmind.comm.sms.model.IncomingSmsEvent;
import com.yahoo.inmind.commons.control.Constants;

/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private SmsSensorActivity mActivity;


    protected ViewHelper(SmsSensorActivity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(SmsSensorActivity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    // ****************************** SMS ***********************************************

    public void stopSms() {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SENSOR_SETTINGS)
                .put(Constants.SENSOR_NAME, Constants.SENSOR_SMS)
                .put(Constants.SENSOR_SETTING, Constants.SENSOR_STOP));
    }

    public void startSms() {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SENSOR_SETTINGS)
                .put(Constants.SENSOR_NAME, Constants.SENSOR_SMS)
                .put(Constants.SENSOR_SETTING, Constants.SENSOR_START)); //in microseconds
    }

    public void onEventMainThread(final IncomingSmsEvent event){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.setPhoneNumber( event.getPhoneNumber() );
                mActivity.setMessage( event.getMessage() );
            }
        });
    }
}
