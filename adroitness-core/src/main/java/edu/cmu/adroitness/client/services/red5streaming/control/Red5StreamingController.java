package edu.cmu.adroitness.client.services.red5streaming.control;

import java.util.ArrayList;

import android.app.Activity;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;

/**
 * Created by sakoju on 7/12/16.
 */
public class Red5StreamingController {
    private static Red5StreamingController instance;
    protected MessageBroker mMB;
    protected Activity mActivity;
    protected static Red5ConfigVO configVO;
    public boolean flag = false;

    static {
        if (configVO == null) {
            configVO = new Red5ConfigVO();
        }
    }

    protected Red5StreamingController(Activity activity) {
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        // you have to subscribe the object who will handle the event responses (that is, those
        // implementing the onEventMainThread method.
        mMB.subscribe( activity );
    }


    public static Red5StreamingController getInstance(Activity activity) {
        if (instance == null) {
            instance = new Red5StreamingController(activity);
        }
        return instance;
    }

    public static Red5StreamingController getInstance() {
        return getInstance(null);
    }

    // ****************************** RED5 STREAMING *****************************************

    public void startStreaming(Object config, Object surfaceView) {

        mMB.send(Red5StreamingController.this, MBRequest.build(Constants.MSG_RED5_STREAMING_START)
                .put(Constants.RED5STREAMING_CONFIG, config)
                .put(Constants.RED5STREAMING_SURFACEVIEW, surfaceView));
    }

    public void stopStreaming() {
        mMB.send(Red5StreamingController.this, MBRequest.build(Constants.MSG_RED5_STREAMING_STOP));
    }

    public void toggleCamera() {
        mMB.send(Red5StreamingController.this, MBRequest.build(Constants.MSG_RED5_STREAMING_TOGGLE_CAMERA) );
    }

    public  ArrayList<String> getCameraSizes() {
        return ( ArrayList<String>) mMB.get(Red5StreamingController.this, MBRequest.build(
                Constants.MSG_RED5_STREAMING_GET_CAMERA_SIZES ));
    }

    public void attachFragment(Activity activity, String isManualStreaming) {
        mMB.send(Red5StreamingController.this, MBRequest.build(Constants.MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT)
                .put(Constants.RED5STREAMING_ACTIVITY, activity)
                .put(Constants.RED5STREAMING_FLAG, isManualStreaming));
    }

    // ****************************** SUBSCRIBE TO MB *****************************************

    /*public void subscribe( Activity subscriber ){
        mMB.subscribe(subscriber);
    }

    public void unsubscribe( Activity subscriber ){
        mMB.unsubscribe(subscriber);
    }*/

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
    public Red5ConfigVO getConfigVO()
    {
        return configVO;
    }
    public void setConfigVO(String host, int port, String appName, String streamName, int bitRate, boolean audio, boolean video)
    {
        configVO.setHost(host);
        configVO.setPort(port);
        configVO.setApp(appName);
        configVO.setName(streamName);
        configVO.setBitrate(bitRate);
        configVO.setAudio(audio);
        configVO.setVideo(video);
    }


}
