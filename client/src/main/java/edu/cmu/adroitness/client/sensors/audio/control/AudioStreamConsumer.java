package edu.cmu.adroitness.client.sensors.audio.control;

/**
 * Created by oscarr on 9/23/15.
 */

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.Toast;

import edu.cmu.adroitness.client.sensors.audio.view.AudioRecordActivity;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.streaming.model.AudioRecordEvent;

/**
 * Helper class to demonstrate that we can have multiple subscribers (consumers) waiting
 * for audio stream updates. When the consumer receives the update, it play the buffer content
 * by using an AudioTrack object
 */
public class AudioStreamConsumer extends Thread{
    private AudioTrack track = null;
    private MessageBroker mb;
    private int requestedSampleRate;
    private int requestedChannelConfig;
    private int requestedAudioEncoding;
    private AudioRecordActivity activity;
    ViewHelper helper;

    private final int[] SAMPLE_RATE = new int[]{ 44100 }; //{ 8000, 11025, 16000, 22050, 44100 };
    private final int[] CHANNEL_CONFIG = new int[] { AudioFormat.CHANNEL_IN_DEFAULT }; //AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.CHANNEL_IN_STEREO
    private final int[] ENCODING = new int[] { AudioFormat.ENCODING_PCM_16BIT };  //AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_DEFAULT, AudioFormat.ENCODING_PCM_8BIT

    public AudioStreamConsumer(String name, AudioRecordActivity activity){
        super( name );
        this.activity = activity;
        helper = ViewHelper.getInstance();
    }

    public void run(){
        mb = MessageBroker.getInstance( activity.getApplicationContext() );
        mb.subscribe( this, AudioRecordEvent.class );
        startRecording();
    }

    /**
     * This method sends a subscription request to the Audio Recorder. If the Audio Recorder
     * is currently being used for another component, then your request (including the config you
     * have specified such as sample rate, channel config, audio encoding, etc.) will be enqueued
     * until the other component releases the audio recorder, however you will receive the recorded
     * stream in the meanwhile.
     */
    public void startRecording(){
        requestedSampleRate = 8000; //SAMPLE_RATE[ (cont - 1) % SAMPLE_RATE.length ];
        requestedChannelConfig = AudioFormat.CHANNEL_IN_MONO; //CHANNEL_CONFIG[ (cont - 1) % CHANNEL_CONFIG.length ];
        requestedAudioEncoding = AudioFormat.ENCODING_PCM_16BIT; //ENCODING[ (cont - 1) % ENCODING.length ];
        helper.startRecording(requestedSampleRate, requestedChannelConfig, requestedAudioEncoding);
    }


    /**
     * On this method you can process the recorded audio stream. For testing purposes, the onEvent
     * method plays the audio stream by using the AudioTrack class.
     * @param event
     */
    public void onEvent( final AudioRecordEvent event ){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( event.getErrorMessage() != null ){
                    Toast.makeText( activity, event.getErrorMessage(), Toast.LENGTH_LONG );
                } else {
                    try {
                        if (track == null || event.isNewConfiguration()) {
                            track = new AudioTrack(AudioManager.STREAM_MUSIC,
                                    event.getSampleRate(),
                                    AudioFormat.CHANNEL_OUT_MONO,
                                    event.getAudioEncoding(),
                                    event.getMinBufferSize(),
                                    AudioTrack.MODE_STREAM);
                            track.play();
                        }
                        track.write(event.getBuffer(), 0, event.getMinBufferSize());
//                    Log.e("TEST", "Receiving audio stream on " + this.getName() + ". Requested sample rate: "
//                            + requestedSampleRate + " but using sample rate: " + event.getSampleRate());
                    }catch(Exception e){
                        //TODO
                    }
                }
            }
        });
    }

    /**
     * The services_streaming_camera purpose of this method is to unsubscribe from receiving audio stream updates. It also
     * releases the resources (in this case, the audio track object).
     */
    public void stopRecording(){
        helper.stopRecording();

        if( track != null ) {
            track.pause();
            track.flush();
            track.release();
            track = null;
        }
    }
}
