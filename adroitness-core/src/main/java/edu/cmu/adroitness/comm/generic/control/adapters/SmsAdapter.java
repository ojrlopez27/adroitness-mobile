package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.effectors.sms.control.SmsEffector;
import edu.cmu.adroitness.client.effectors.sms.model.SmsVO;

/**
 * Created by oscarr on 6/22/16.
 */
public class SmsAdapter extends ChannelAdapter {
        private static SmsAdapter instance;

        private SmsAdapter() {
            super();
        }

    public static SmsAdapter getInstance() {
        if (instance == null) {
            instance = new SmsAdapter();
        }
        return instance;
    }

    public void sendSms(final MBRequest request){
        mResourceLocator.addRequest( SmsEffector.class, "sendSms",
                ( (SmsVO) request.get(Constants.EFFECTOR_DATA)));
    }
}
