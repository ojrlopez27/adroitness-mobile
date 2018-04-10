package edu.cmu.adroitness.client.effectors.sms.control;

import android.content.Context;
import android.os.Handler;
import android.telephony.SmsManager;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.effectors.generic.control.EffectorObserver;
import edu.cmu.adroitness.client.effectors.sms.model.SmsVO;

/**
 * Created by oscarr on 9/29/15.
 */
public final class SmsEffector extends EffectorObserver {
    private SmsManager smsManager;

    public SmsEffector(Handler handler, Context context) {
        super(handler, context);
        this.actions.add(Constants.ACTION_SEND_SMS);
        smsManager = SmsManager.getDefault();
    }

    public void sendSms(final SmsVO smsVO ){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    smsManager.sendTextMessage(smsVO.getPhoneNumber(), null, smsVO.getMessage(), null, null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
