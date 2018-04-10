package edu.cmu.adroitness.client.services.generic.control;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;

import edu.cmu.adroitness.client.commons.control.ResourceLocator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oscarr on 8/5/15.
 */
    public abstract class GenericService extends IntentService {
    protected static Context mContext;
    protected static ResourceLocator resourceLocator;
    protected String mAwarePackageName;
    protected HashMap<Integer, MBRequest> requests;
    protected ArrayList<BroadcastReceiver> receivers;
    /**
     * These are the filters used to register this service to the corresponding BroadcastReceiver
     * (ServiceReceiver)
     */
    protected ArrayList<String> actions;

    /** Communication **/
    protected static MessageBroker mb;

    /** Binder given to clients */
    protected IBinder mBinder = new ResourceLocator.MiddServiceBinder();

    public GenericService(){
        super("");
        mb = MessageBroker.getInstance(null);
        mContext = ResourceLocator.getContext();
        receivers = new ArrayList<>();
        resourceLocator = ResourceLocator.getInstance(mContext);
        actions = new ArrayList<>();
    }

    public GenericService(String awarePackageName){
        this();
        mAwarePackageName = awarePackageName;
    }

    public HashMap<Integer, MBRequest> getRequests() {
        return requests;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void start() {
        if(mAwarePackageName != null) {
            AwareServiceWrapper.startPlugin(mContext, mAwarePackageName);
        }
    }

    public void stop() {
        if(mAwarePackageName != null) {
            AwareServiceWrapper.stopPlugin(mContext, mAwarePackageName);
        }
        stopSelf();
    }

    public GenericService register(BroadcastReceiver receiver, ArrayList<String> actions) {
        IntentFilter contextFilter = new IntentFilter();
        for (String action : actions) {
            contextFilter.addAction(action);
        }
        mContext.registerReceiver(receiver, contextFilter);
        if( !receivers.contains( receiver ) ) {
            receivers.add(receiver);
        }
        return this;
    }

    public void unregister(BroadcastReceiver receiver) {
        try {
            receivers.remove(receiver);
            mContext.unregisterReceiver(receiver);
        }catch (IllegalArgumentException e) {
            //do nothing
        }
    }

    public void unregister(){
        if( receivers != null ) {
            for (BroadcastReceiver receiver : receivers) {
                unregister(receiver);
            }
            receivers = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if( mContext == null ) {
            mContext = getApplicationContext();
        }
        ((ResourceLocator.MiddServiceBinder) mBinder).setService(this);
        return mBinder;
    }


    private void subscribe(){
        Method[] methods = this.getClass().getMethods();
        for( Method method : methods ){
            if( method.getName().startsWith("onEvent") ){
                mb.subscribe(this);
                break;
            }
        }
    }


    public void doAfterBind(){
        mb = MessageBroker.getExistingInstance( this );
        mb.subscribe(this); //subscribe();
        start();
        resourceLocator.processRequests( this.getClass() );
    }


    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
        stop();
        requests = null;
        receivers = null;
        actions = null;
        System.gc();
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public void setActions(ArrayList<String> actions) {
        this.actions = actions;
    }

    protected void onHandleIntent(Intent intent) {
        //do nothing. We need this to run aware services
    }
}
