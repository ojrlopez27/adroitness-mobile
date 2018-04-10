package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.streaming.model.AudioRecordEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.sensors.audio.control.AudioController;

/**
 * Created by oscarr on 3/15/16.
 */
public final class AudioAdapter extends ChannelAdapter {
    private static AudioAdapter instance;

    private AudioAdapter() {
        super();
    }

    public static AudioAdapter getInstance() {
        if (instance == null) {
            instance = new AudioAdapter();
        }
        return instance;
    }

    public void recordAudio(MBRequest mbRequest) {
        mMB.removeSubscriptionException(mbRequest.get(Constants.SET_SUBSCRIBER), AudioRecordEvent.class);
        AudioController.getInstance((Integer) mbRequest.get(
                Constants.SET_AUDIO_SAMPLE_RATE),
                (Integer) mbRequest.get(Constants.SET_AUDIO_CHANNEL_CONFIG),
                (Integer) mbRequest.get(Constants.SET_AUDIO_ENCODING),
                (Integer) mbRequest.get(Constants.SET_AUDIO_BUFFER_ELEMENTS_TO_REC),
                (Integer) mbRequest.get(Constants.SET_AUDIO_BYTES_PER_ELEMENT),
                mMB,
                mbRequest.get(Constants.SET_SUBSCRIBER));
    }

    public void stopRecordAudio(MBRequest request) {
        mMB.unsubscribe(request.get(Constants.SET_SUBSCRIBER), AudioRecordEvent.class);
        AudioController.getInstance( mMB ).unsubscribe(request.get(Constants.SET_SUBSCRIBER));
    }
}
