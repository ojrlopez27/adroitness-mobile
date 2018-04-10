package edu.cmu.adroitness.comm.googlespeechrecognition.model;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by sakoju on 4/16/17.
 */

public class GoogleSpeechRecognitionEvent extends BaseEvent {
    private GoogleSpeechRecognitionEvent(){ super(); }
    private GoogleSpeechRecognitionEvent(int mbRequestId){ super( mbRequestId); }

    public static GoogleSpeechRecognitionEvent build(){
        return new GoogleSpeechRecognitionEvent();
    }
    public static GoogleSpeechRecognitionEvent build(int mbRequestId){
        return new GoogleSpeechRecognitionEvent(mbRequestId);
    }

    public SpeechRecognitionAlternative getAlternative() {
        return alternative;
    }

    public GoogleSpeechRecognitionEvent setAlternative(SpeechRecognitionAlternative alternative) {
        this.alternative = alternative;
        return this;
    }

    public SpeechRecognitionAlternative alternative;

    public Boolean getFinal() {
        return isFinal != null && isFinal;
    }

    public GoogleSpeechRecognitionEvent setFinal(Boolean aFinal) {
        isFinal = aFinal;
        return this;
    }

    public Boolean isFinal;
    public String notification;

    public String getNotification() {
        return notification;
    }

    public GoogleSpeechRecognitionEvent setNotification(String notification) {
        this.notification = notification;
        return this;
    }


}
