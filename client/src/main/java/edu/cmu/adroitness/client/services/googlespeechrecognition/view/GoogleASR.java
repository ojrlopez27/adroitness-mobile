package edu.cmu.adroitness.client.services.googlespeechrecognition.view;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.services.googlespeechrecognition.control.ViewHelper;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.nlg.model.NLGEvent;

public class GoogleASR extends AppCompatActivity {
    public static ViewHelper viewHelper;
    MessageBroker mb;
    public Button asrButton;
    public TextView asrResult;
    public Boolean isStarted = false, isStopped = false;
    GoogleASR activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.services_google_asr_activity);
        asrButton = (Button) findViewById(R.id.asrButton);
        asrResult = (TextView) findViewById(R.id.asrResult);
        asrResult.setMovementMethod(new ScrollingMovementMethod());

        mb = MessageBroker.getInstance(getApplicationContext());
        viewHelper = ViewHelper.getInstance(this);
        viewHelper.subscribe(this);
        activity = this;
        asrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asrButton.getText().toString().contains("Start")) {
                    viewHelper.startVoiceRecorder();
                }
                else
                {
                    viewHelper.stopVoiceRecorder();
                }
            }
        });
    }

    /**
     * This is the message sent by NLG from Multiuser framework
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final NLGEvent event){
        Log.d("GoogleASR", event.getOutput() );
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.services_google_asr_activity);

    }
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewHelper.subscribe( this );
        //viewHelper.startStreaming();
    }

    @Override
    protected void onPause() {
        viewHelper.stopVoiceRecorder();
        viewHelper.stopStreaming();
        viewHelper.unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        viewHelper.stopVoiceRecorder();
       viewHelper.stopStreaming();
        viewHelper.unsubscribe(this);
        super.onDestroy();
    }

    //Send animation request (BSON) to Unity
    public void sendMessageToUnity(String message){
       // UnityPlayer.UnitySendMessage("Sara_newSmile_v4", "receiveMessageFromAndroid", message);
    }

}
