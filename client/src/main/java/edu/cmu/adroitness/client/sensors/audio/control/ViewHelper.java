package edu.cmu.adroitness.client.sensors.audio.control;

import android.app.Activity;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.sensors.audio.view.AudioRecordActivity;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;


import java.util.ArrayList;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private ArrayList<AudioStreamConsumer> audioStreamConsumers;
    private int cont = 0;
    private Activity mActivity;

    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    public static ViewHelper getInstance() {
        if (instance == null) {
            instance = new ViewHelper( null );
        }
        return instance;
    }

    // ****************************** AUDIO RECORDING ***********************************************

    public void startRecording(int requestedSampleRate, int requestedChannelConfig,
                               int requestedAudioEncoding){
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_START_AUDIO_RECORD)
                .put(Constants.SET_AUDIO_SAMPLE_RATE, requestedSampleRate)
                .put(Constants.SET_AUDIO_CHANNEL_CONFIG, requestedChannelConfig)
                .put(Constants.SET_AUDIO_ENCODING, requestedAudioEncoding)
                        // 2 bytes in 16bit format
                .put(Constants.SET_AUDIO_BYTES_PER_ELEMENT, 2)
                        // we want to play 2048 (2K). Since 2 bytes, we use only 1024
                .put(Constants.SET_AUDIO_BUFFER_ELEMENTS_TO_REC, 1024)
                .put(Constants.SET_SUBSCRIBER, this));
    }

    public void stopRecording(){
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_STOP_AUDIO_RECORD)
                .put(Constants.SET_SUBSCRIBER, this));
    }

    public void destroy() {
        for( AudioStreamConsumer c : audioStreamConsumers){
            c.stopRecording();
            c.interrupt();
        }
    }

    /**
     * Creates a new thread AudioStreamConsumer that subscribes to the audio stream updates
     */
    public void start( AudioRecordActivity activity ){
        AudioStreamConsumer c = new AudioStreamConsumer( "AudioStreamConsumer" + cont++, activity );
        c.start();
        audioStreamConsumers.add( c );
    }

    /**
     * Stops the execution of the consumer thread and as a consequence, no more audio stream
     * updates are sent to this thread.
     */
    public void stop( ){
        if( audioStreamConsumers.isEmpty() == false ) {
            AudioStreamConsumer c = audioStreamConsumers.remove( 0 );
            c.stopRecording();
            c.interrupt();
        }
    }
}
