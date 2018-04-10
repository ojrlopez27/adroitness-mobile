package edu.cmu.adroitness.client.services.generic.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by oscarr on 8/13/15.
 */
public class ServiceDataReceiver extends BroadcastReceiver {
    private Context mContext;

    public ServiceDataReceiver(Context context){
        mContext = context;
    }

    public ServiceDataReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
//        Util.execute(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("", "Inside ServiceDataReceiver.onReceive");
//            }
//        });
    }
}
