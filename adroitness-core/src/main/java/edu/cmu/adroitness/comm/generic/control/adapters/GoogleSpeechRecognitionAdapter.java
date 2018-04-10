package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.googlespeechrecognition.control.GoogleSpeechRecognitionService;

/**
 * Created by sakoju on 4/16/17.
 */

public class GoogleSpeechRecognitionAdapter extends ChannelAdapter
{
    private static GoogleSpeechRecognitionAdapter instance;

    private GoogleSpeechRecognitionAdapter() {
        super();
    }

    public static GoogleSpeechRecognitionAdapter getInstance()
    {
        if(instance == null)
        {
            instance = new GoogleSpeechRecognitionAdapter();
        }
        return instance;
    }

    public void startVoiceRecorder( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GoogleSpeechRecognitionService.class, "startVoiceRecorder");
    }

    public void stopVoiceRecorder( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GoogleSpeechRecognitionService.class, "stopVoiceRecorder");
    }

    /**
     * This method forwards the ASR output to the Multiuser Framework server
     * @param request
     */
    public void setIpAddress(final MBRequest request) {
        mResourceLocator.addRequest(GoogleSpeechRecognitionService.class, "setIpAddress",
                (request.get(Constants.MULTIUSER_IP_ADDRESS, new String())));
    }
}