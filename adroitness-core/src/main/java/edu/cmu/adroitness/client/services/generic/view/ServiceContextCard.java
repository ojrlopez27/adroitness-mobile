package edu.cmu.adroitness.client.services.generic.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.aware.ui.Stream_UI;
import com.aware.utils.IContextCard;

/**
 * Created by oscarr on 8/17/15.
 */
public class ServiceContextCard implements IContextCard {
    protected int refresh_interval = 60 * 1000; //once a minute
    //Declare here all the UI elements you'll be accessing
    protected View card;
    //You may use sContext on uiChanger to do queries to databases, etc.
    protected Context sContext;
    protected Handler uiRefresher = new Handler(Looper.getMainLooper());
    protected Runnable uiChanger;
    //This is a BroadcastReceiver that keeps track of stream status. Used to stop the refresh when user leaves the stream and restart again otherwise
    protected StreamObs streamObs = new StreamObs();
    protected LayoutInflater inflater;
    protected boolean registered = false;


    public class StreamObs extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( intent.getAction().equals(Stream_UI.ACTION_AWARE_STREAM_OPEN) ) {
                //start refreshing when user enters the stream
                uiRefresher.postDelayed(uiChanger, refresh_interval);
            }
            if( intent.getAction().equals(Stream_UI.ACTION_AWARE_STREAM_CLOSED) ) {
                //stop refreshing when user leaves the stream
                uiRefresher.removeCallbacks(uiChanger);
                uiRefresher.removeCallbacksAndMessages(null);
            }
        }
    }


    @Override
    public View getContextCard(Context context) {
        sContext = context;
        registerReceiver(context);
        //Begin refresh cycle
        uiRefresher.post(uiChanger);
        return card;
    }

    public void registerReceiver(Context context){
        if( !registered ) {
            Log.e("", "REGISTER: "+ this.getClass().getName() + "  status: " + registered);
            //Tell Android that you'll monitor the stream statuses
            IntentFilter filter = new IntentFilter();
            filter.addAction(Stream_UI.ACTION_AWARE_STREAM_OPEN);
            filter.addAction(Stream_UI.ACTION_AWARE_STREAM_CLOSED);
            context.registerReceiver(streamObs, filter);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            registered = true;
        }
    }

    public void unregisterReceiver(Context context){
        Log.e("", "UNREGISTER: "+ this.getClass().getName() + "  status: " + registered);
        context.unregisterReceiver(streamObs);
        registered = false;
    }
}
