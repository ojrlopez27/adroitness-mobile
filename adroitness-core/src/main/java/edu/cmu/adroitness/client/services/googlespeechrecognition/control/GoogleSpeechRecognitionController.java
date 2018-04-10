package edu.cmu.adroitness.client.services.googlespeechrecognition.control;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.googlespeechrecognition.model.GoogleSpeechRecognitionEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.googlespeechrecognition.view.GoogleSpeechRecognitionActivity;

/**
 * Created by sakoju on 4/16/17.
 */

public class GoogleSpeechRecognitionController {

    private static GoogleSpeechRecognitionController instance;
    private GoogleSpeechRecognitionService mSpeechService;
    protected MessageBroker mMB;
    protected Activity mActivity;
    public Context context;

    // View references
    private TextView mStatus;
    private TextView mText;
    private GoogleSpeechRecognitionActivity.ResultAdapter mAdapter;
    private RecyclerView mRecyclerView;

    protected GoogleSpeechRecognitionController(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( mActivity );
        mMB.subscribe(this);
        mStatus = ((GoogleSpeechRecognitionActivity)mActivity).getmStatus();
        mText = ((GoogleSpeechRecognitionActivity)mActivity).getmText();
        mAdapter = ((GoogleSpeechRecognitionActivity)mActivity).getmAdapter();
        mRecyclerView = ((GoogleSpeechRecognitionActivity)mActivity).getmRecyclerView();
    }

    public static GoogleSpeechRecognitionController getInstance(Activity activity) {
        if (instance == null) {
            instance = new GoogleSpeechRecognitionController(activity);
        }
        return instance;
    }
    public void subscribe( Activity subscriber ){
        mMB.subscribe(subscriber);
    }

    public void unsubscribe( Activity subscriber ){
        mMB.unsubscribe(subscriber);
    }

    private void showStatus(final boolean hearingVoice) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setTextColor(hearingVoice ?
                        ((GoogleSpeechRecognitionActivity) mActivity).getmColorHearing() :
                        ((GoogleSpeechRecognitionActivity) mActivity).getmColorNotHearing());
            }
        });
    }

    public void startVoiceRecorder()
    {
        mMB.send(GoogleSpeechRecognitionController.this, MBRequest.build(Constants.MSG_GOOGLE_SPEECH_START_ASR));
    }

    public void stopVoiceRecorder()
    {
        mMB.send(GoogleSpeechRecognitionController.this, MBRequest.build(Constants.MSG_GOOGLE_SPEECH_END_ASR));
    }

    public void onEventMainThread(final GoogleSpeechRecognitionEvent googleSpeechRecognitionEvent)
    {
        if(googleSpeechRecognitionEvent.getAlternative()!=null )
        {
            if (mText != null && !TextUtils.isEmpty(googleSpeechRecognitionEvent
                    .getAlternative().getTranscript())) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (googleSpeechRecognitionEvent.getFinal()) {
                            mText.setText(null);
                            Log.d("Speech Service Activity", googleSpeechRecognitionEvent
                                    .getAlternative().getTranscript());
                            mAdapter.addResult(googleSpeechRecognitionEvent
                                    .getAlternative().getTranscript());
                            mRecyclerView.smoothScrollToPosition(0);
                        } else {
                            mText.setText(googleSpeechRecognitionEvent
                                    .getAlternative().getTranscript());
                        }
                    }
                });
            }
        }
    }

}
