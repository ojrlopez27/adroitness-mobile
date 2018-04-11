package edu.cmu.adroitness.client.services.googlespeechrecognition.control;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.SurfaceView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.googlespeechrecognition.view.GoogleASR;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;
import edu.cmu.adroitness.client.services.red5streaming.view.SurfaceCameraFragment;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.comm.googlespeechrecognition.model.GoogleSpeechRecognitionEvent;
import edu.cmu.adroitness.comm.nlg.model.NLGEvent;
import edu.cmu.adroitness.comm.red5streaming.model.Red5StreamingEvent;

/**
 * Created by sakoju on 4/21/17.
 */

public class ViewHelper {
    private static ViewHelper instance;
    protected MessageBroker mMB;
    protected Activity mActivity;
    public Context context;
    protected Red5ConfigVO configVO;
    public boolean flag = false;
    boolean isStreaming=false;
    protected SurfaceView surfaceForCamera;


    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        ArrayList<String> services = new ArrayList();
        services.add( Constants.ADD_SERVICE_RED5STREAMING );
        services.add( Constants.ADD_SERVICE_MULTIUSER );
        services.add( Constants.ADD_SERVICE_ASR_NLG );
        services.add( Constants.ADD_SERVICE_GOOGLE_SPEECH_RECOGNITION );
        mMB = MessageBroker.getInstance( mActivity, services );
        mMB.subscribe(this);
        //Utils.loadAssets( mActivity.getApplicationContext(), "configuration.properties");
        initialize();
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    public void subscribe( Activity subscriber ){
        mMB.subscribe(subscriber);
        mActivity = subscriber;
    }

    public void unsubscribe( Activity subscriber ){
        mMB.unsubscribe(subscriber);
    }


    /******************************************* Google Cloud Speech *****************************************/

    /***
     * The method starts ASR including a Voice Recorder, Speech recognition and authentication.
     */
    public void startVoiceRecorder()
    {
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_GOOGLE_SPEECH_START_ASR));
    }

    /***
     * The method stops ASR including a Voice Recorder, Speech recognition
     * and access Token renewals.
     */
    public void stopVoiceRecorder()
    {
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_GOOGLE_SPEECH_END_ASR));
    }

    /***************************************** Red5 Streaming ********************************************/
    public void startStreaming() {

        if(!isStreaming) {
            surfaceForCamera = SurfaceCameraFragment.getSurfaceView();

            mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_RED5_STREAMING_START)
                    .put(Constants.RED5STREAMING_SURFACEVIEW, surfaceForCamera)
                    .put(Constants.RED5STREAMING_CONFIG, configVO));
            isStreaming=true;
        }
    }

    public void stopStreaming() {
        if (isStreaming) {
            mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_RED5_STREAMING_STOP));
            isStreaming = false;
        }
    }

    public void attachFragment(Activity activity, String isManualStreaming) {
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT)
                .put(Constants.RED5STREAMING_ACTIVITY, activity)
                .put(Constants.RED5STREAMING_FLAG, isManualStreaming));
    }

    public void createConfigVO()
    {
        configVO = new Red5ConfigVO();
        configVO.setHost("34.203.204.136");
        configVO.setPort(8554);
        configVO.setApp("live");
        //TODO
        configVO.setName("myStream" + Util.getDeviceId( mActivity.getApplicationContext() ) ); // it should be unique
        configVO.setBitrate(128);
        configVO.setAudio(false);
        configVO.setVideo(true);
        flag = true;
    }

    public void initialize()
    {
        if(!flag || configVO == null) {
            createConfigVO();
        }

        attachFragment(mActivity, "true");
    }

    /****************************** MultiUser Framework *****************************************/
    /**
     * This is the ip address where Multiuser Framework is running
     * @param ipAddress
     */
    void startMultiuser(String ipAddress){
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_MULTIUSER_START)
                        .put(Constants.MULTIUSER_IP_ADDRESS, ipAddress) );
    }

    public void stopMultiuser(){
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_MULTIUSER_STOP));
    }

    /************* event Handlers ********************/
    /**
     * Response from NLG (MUF)
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final NLGEvent event){
        ((GoogleASR)mActivity).sendMessageToUnity( event.getOutput() );
        Log.d("NLGEvent", event.getOutput() );
    }

    /**
     * Now, Middleware is ready to process messages so we can start
     * the Multiuser Framework
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MiddlewareNotificationEvent event){
        startMultiuser( "ipaddress") ;
        Log.d("MiddlewareEvent", event.getMessage() );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final Red5StreamingEvent event){
       if(event.getServiceStatus().equals(Constants.RED5_STREAMING_STATUS_READY))
       {
           startStreaming();
           Log.d("Red5Streaming", event.getServiceStatus());
       }
       if(event.getServiceStatus().equals(Constants.RED5_STREAMING_STATUS_START_STREAMING))
       {
           //mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_MULTIUSER_SEND)
             //      .put(Constants.MULTIUSER_SEND_MESSAGE, new SessionMessage("MSG_START_SESSION", "")));
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final GoogleSpeechRecognitionEvent googleSpeechRecognitionEvent) {
        if(googleSpeechRecognitionEvent.getNotification()!=null) {
            if (googleSpeechRecognitionEvent.getNotification().equals(Constants.GOOGLE_SPEECH_START_NOTIFICATION)) {

                ((GoogleASR)mActivity).asrResult.setText("\nSpeech API has been started!");
                ((GoogleASR)mActivity).isStarted = new Boolean(true);
                ((GoogleASR)mActivity).isStopped = new Boolean(false);

            }
            if(googleSpeechRecognitionEvent.getNotification().equals(Constants.GOOGLE_SPEECH_STOP_NOTIFICATION))
            {
                ((GoogleASR)mActivity).asrResult.append("\nSpeech API has been stopped!");
                ((GoogleASR)mActivity).asrButton.setText("Start ASR");
                ((GoogleASR)mActivity).isStopped = new Boolean(true);
                ((GoogleASR)mActivity).isStarted = new Boolean(false);
            }
        }
        if (googleSpeechRecognitionEvent.getAlternative() != null) {
            if(googleSpeechRecognitionEvent.getFinal())
            {

                ((GoogleASR)mActivity).asrResult.append("\nSpeech text: " +
                                googleSpeechRecognitionEvent.getAlternative().getTranscript()
                                + " , confidence:"
                                + googleSpeechRecognitionEvent.getAlternative().getConfidence());
                ((GoogleASR)mActivity).asrResult.setMovementMethod(new ScrollingMovementMethod());

                Log.d("GoogleASR", "\nSpeech text: " +
                        googleSpeechRecognitionEvent.getAlternative().getTranscript()
                        + " , confidence:" +
                        googleSpeechRecognitionEvent.getAlternative().getConfidence());
                if(((GoogleASR)mActivity).asrButton.getText().toString().contains("Start"))
                {
                    ((GoogleASR)mActivity).asrButton.setText("Stop ASR");
                }
            }

        }

    }



}
