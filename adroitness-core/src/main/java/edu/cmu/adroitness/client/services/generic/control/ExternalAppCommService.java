package edu.cmu.adroitness.client.services.generic.control;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;

import java.util.HashMap;

public class ExternalAppCommService extends GenericService {
    private final String TAG = ExternalAppCommService.class.getName();
    private Messenger mMessenger;
    private HashMap<Integer, Messenger> registeredMessengers;
    private ResourceLocator resourceLocator;

    public ExternalAppCommService() {
        super(null);
        if( actions.isEmpty() ){
            this.actions.add(Constants.ACTION_EXTERNAL_COMMUNICATION );
        }
        mMessenger = new Messenger( new IncomingMessageHandler() );
        registeredMessengers = new HashMap<>();
        resourceLocator = ResourceLocator.getExistingInstance();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        if( arg0.getAction() == null )
            return super.onBind(arg0);
        return mMessenger.getBinder();
    }

    class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case Constants.REGISTER:
                    registeredMessengers.put( msg.arg1, msg.replyTo );
                    break;
                case Constants.UNREGISTER:
                    registeredMessengers.remove( msg.arg1 );
                    break;
                case Constants.RESPONSE:
//                    if( msg.arg1 == Constants.ID_APP_TRACKER) {
//                        resourceLocator.lookupService(AppTrackerService.class )
//                                .processResponse( msg.arg2, msg.getData() );
//                    }else if( msg.arg1 == Constants.ID_HELPR) {
//                        resourceLocator.lookupService(HELPRService.class )
//                                .processResponse( msg.arg2, msg.getData() );
//                    }
                    break;
                case Constants.RESPONSE_EXCEPTION:
                    Log.e( TAG, "arg1: " + msg.arg1 );
                    Log.e( TAG, "arg2: " + msg.arg2 );
                    Log.e( TAG, "obj: " + msg.obj );
                    break;
                default:
                    Log.e( TAG, "Not supported incoming message" );
                    break;
            }
        }
    }

    public void sendMessage( int appId, int what, int arg1, int arg2, Object obj){
        sendMessage(appId, what, arg1, arg2, obj, null);
    }

    public void sendMessage( int appId, int what, int arg1, int arg2, Object obj, Bundle data){
        Messenger messenger = registeredMessengers.get( appId );
        if( messenger != null ){
            Message message = Message.obtain(null, what, arg1, arg2);
            try {
                if( obj != null && obj instanceof String ){
                    if( data == null ) {
                        data = new Bundle();
                    }
                    data.putString( "request", (String) obj );
                    message.setData( data );
                }
                messenger.send(message);
            } catch (RemoteException rme) {
                Log.e( TAG, rme.getMessage() );
                rme.printStackTrace();
            }
        }else{
            Log.e( TAG, "App ID is not a valid id");
        }
    }
}
