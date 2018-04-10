package edu.cmu.adroitness.client.services.red5streaming.control;

import java.util.ArrayList;

import android.app.Fragment;
import android.util.Log;
import android.view.SurfaceView;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import edu.cmu.adroitness.client.commons.control.Constants;

/**
 * Created by sakoju on 1/27/17.
 */

public class Red5StreamListener {

    private R5ConnectionEvent r5ConnectionEvent;
    String streamStatus ="";

    public String getStreamState() {
        return streamState;
    }

    public void Red5SteamListener()
    {

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    String streamState= Constants.RED5_STREAMING_STATUS_DISCONNECTED;
    String errorMessage;
    R5Stream stream;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    String streamName;
    public void startListener(final R5Stream stream)
    {
        this.stream  = stream;
        Log.d("Red5StreamListener", "*** inside startListener");
        //set up our listener
        stream.setListener(new R5ConnectionListener() {
            @Override
            public void onConnectionEvent(R5ConnectionEvent r5event) {
                Log.d("Red5StreamListener", "*** inside onConnectionEvent");
                //this is getting called from the network thread, so handle appropriately
                r5ConnectionEvent= r5event;
                switch (r5event) {
                    case CONNECTED:
                        Log.d("Red5StreamListener","R5 Stream Listener - Connected");
                        streamStatus= Constants.RED5_STREAMING_STATUS_CONNECTED;
                        break;
                    case DISCONNECTED:
                        Log.d("Red5StreamListener","R5 Stream Listener - Disconnected");
                        streamStatus= Constants.RED5_STREAMING_STATUS_DISCONNECTED;
                        streamState= Constants.RED5_STREAMING_STATUS_DISCONNECTED;
                        Red5StreamingService.sendRed5StreamingEvent(streamState);
                        break;
                    case START_STREAMING:
                        Log.d("Red5StreamListener","R5 Stream Listener - Started Streaming");
                        streamName = stream.connection.getConfiguration().getStreamName();
                        streamState= Constants.RED5_STREAMING_STATUS_START_STREAMING;
                        streamStatus= Constants.RED5_STREAMING_STATUS_START_STREAMING;
                        Red5StreamingService.sendRed5StreamingEvent(streamState);
                        getStreamStatus();
                        break;
                    case STOP_STREAMING:
                        Log.d("Red5StreamListener","R5 Stream Listener - Stopped Streaming");
                        streamState= Constants.RED5_STREAMING_STATUS_STOP_STREAMING;
                        streamStatus= Constants.RED5_STREAMING_STATUS_STOP_STREAMING;
                        Red5StreamingService.sendRed5StreamingEvent(streamState);
                        getStreamStatus();
                        break;
                    case CLOSE:
                        Log.d("Red5StreamListener","R5 Stream Listener - Close");
                        streamStatus= Constants.RED5_STREAMING_STATUS_CLOSE;
                        streamStatus= Constants.RED5_STREAMING_STATUS_CLOSE;
                        Red5StreamingService.sendRed5StreamingEvent(streamState);
                        getStreamStatus();
                        break;
                    case TIMEOUT:
                        Log.d("Red5StreamListener","R5 Stream Listener - Timeout");
                        streamStatus= Constants.RED5_STREAMING_STATUS_TIMEOUT;
                        break;
                    case ERROR:
                        Log.d("Red5StreamListener","R5 Stream Listener - Error: " + r5event.message);
                        errorMessage = r5event.message;
                        streamStatus= Constants.RED5_STREAMING_STATUS_ERROR;
                        getStreamStatus();
                        break;
                }
            }
        });
    }
    ArrayList<Red5StreamListener> red5StreamListeners = new ArrayList<>();

    public static ArrayList<Red5StreamListenerObserver> getRed5StreamListenerObservers() {
        return red5StreamListenerObservers;
    }

    public static void setRed5StreamListenerObservers(ArrayList<Red5StreamListenerObserver> red5StreamListenerObservers) {
        Red5StreamListener.red5StreamListenerObservers = red5StreamListenerObservers;
    }

    static ArrayList <Red5StreamListenerObserver> red5StreamListenerObservers = new ArrayList<>();

    public Red5StreamListener getStreamStatus() {
        for( Red5StreamListenerObserver red5StreamListenerInterface : red5StreamListenerObservers)
        {
            red5StreamListenerInterface.onStreamEvent(this);
        }
        return this;
    }

    public void subscribeToListener(Red5StreamListenerObserver listener)
    {
        if( !red5StreamListenerObservers.contains( listener ) ) {
            red5StreamListenerObservers.add(listener);
        }
    }

    public interface Red5StreamListenerObserver
    {
        void onStreamEvent(Red5StreamListener listener);

    }

    public interface Red5CameraFragmentListener
    {
        void notifySurfaceViewCreated(SurfaceView surfaceView);
        void notifyFragmentOnPause(Fragment fragment);
    }
    public interface Red5SurfaceViewListener
    {
        void notifySurfaceDestroyed(SurfaceView surfaceView);
    }

}
