package edu.cmu.adroitness.effectors.generic.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.effectors.alarm.control.AlarmEffector;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;

/**
 * Created by oscarr on 9/29/15.
 */
public class EffectorDataReceiver extends BroadcastReceiver {
    private MessageBroker mb;
    private Context mContext;
    private ResourceLocator resourceLocator;

    public EffectorDataReceiver(Context context) {
        mContext = context;
    }

    // This is required by the AndroidManifest
    public EffectorDataReceiver() {}

    private void initialize( Context context ){
        if( mb == null ){
            mb = MessageBroker.getInstance( context );
        }
        if( resourceLocator == null ){
            resourceLocator = ResourceLocator.getInstance(context);
        }
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                initialize(mContext);
                if (intent.getAction().equals(Constants.ACTION_SET_ALARM)) {
                    resourceLocator.lookupEffector(AlarmEffector.class).setAlarm(intent);
                }else if (intent.getAction().equals(Constants.ACTION_SEND_SMS)) {
                    //TODO: resourceLocator.lookupEffector(SmsEffector.class).setAlarm(intent);
                }

            }
        });
    }
}
