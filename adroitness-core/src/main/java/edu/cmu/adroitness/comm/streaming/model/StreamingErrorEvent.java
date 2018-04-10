package edu.cmu.adroitness.comm.streaming.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 10/7/15.
 */
public class StreamingErrorEvent extends BaseEvent{
    private String message;
    private String videoQuality;
    private String errorType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
