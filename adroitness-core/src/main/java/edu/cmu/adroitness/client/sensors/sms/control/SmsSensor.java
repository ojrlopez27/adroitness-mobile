package edu.cmu.adroitness.client.sensors.sms.control;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;


import edu.cmu.adroitness.comm.sms.model.IncomingSmsEvent;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.sensors.generic.control.SensorObserver;

/**
 * Created by oscarr on 6/21/16.
 */
public class SmsSensor extends SensorObserver{
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     * @param context
     */
    public SmsSensor(Handler handler, Context context) {
        super(handler, context);
        this.actions.add(SMS_RECEIVED);
    }

    public void processIncomingSMS(final Bundle bundle) {
        if( enable ) {
            Util.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (bundle != null) {
                            final Object[] pdusObj = (Object[]) bundle.get("pdus");
                            String phoneNumber = "", message = "";
                            for (int i = 0; i < pdusObj.length; i++) {
                                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                                phoneNumber = currentMessage.getDisplayOriginatingAddress();
                                message += currentMessage.getDisplayMessageBody() + " ";
                            }
                            mb.send(SmsSensor.this, IncomingSmsEvent.build()
                                    .setPhoneNumber( phoneNumber )
                                    .setMessage( message ));
                        }
                    } catch (Exception e) {
                        Log.e("SmsReceiver", "Exception smsReceiver" + e);
                    }
                }
            });
        }
    }
}
