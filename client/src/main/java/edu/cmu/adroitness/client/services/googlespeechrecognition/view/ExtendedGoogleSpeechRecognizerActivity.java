package edu.cmu.adroitness.client.services.googlespeechrecognition.view;

import android.os.Bundle;
import android.os.PersistableBundle;

import edu.cmu.adroitness.client.services.googlespeechrecognition.control.ViewHelper;

public class ExtendedGoogleSpeechRecognizerActivity extends GoogleSpeechRecognitionActivity {
    private ViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_extended_google_speech_recognizer);

        helper = ViewHelper.getInstance(this);
        helper.subscribe(this);
        //helper.setIpAddress("tcp://128.237.131.52:5555"); //Multiuser Framework (Florian's machine)
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        helper.startVoiceRecorder();
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        helper.startVoiceRecorder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.stopVoiceRecorder();
    }
}
