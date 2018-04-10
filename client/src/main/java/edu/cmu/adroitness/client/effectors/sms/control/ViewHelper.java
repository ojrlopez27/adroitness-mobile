package edu.cmu.adroitness.client.effectors.sms.control;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.effectors.sms.model.SmsVO;
import edu.cmu.adroitness.client.effectors.sms.view.SmsEffectorActivity;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;

/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private SmsEffectorActivity mActivity;


    protected ViewHelper(SmsEffectorActivity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(SmsEffectorActivity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    // ****************************** SMS ***********************************************

    public void sendSMS(SmsVO smsVO) {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_EFFECTOR_SEND_SMS)
                .put(Constants.SENSOR_NAME, Constants.SENSOR_SMS)
                .put(Constants.EFFECTOR_DATA, smsVO));
    }
}
