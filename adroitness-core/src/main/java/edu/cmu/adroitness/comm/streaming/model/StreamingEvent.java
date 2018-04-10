package edu.cmu.adroitness.comm.streaming.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 10/7/15.
 */
public class StreamingEvent extends BaseEvent {
    private Object type;
    private int message;
    private long bitRate;
    private int camera;

    private StreamingEvent(){ super(); }
    public static StreamingEvent build(){
        return new StreamingEvent();
    }

    public Object getType() {
        return type;
    }

    public StreamingEvent setType(Object type) {
        this.type = type;
        return this;
    }

    public int getMessage() {
        return message;
    }

    public StreamingEvent setMessage(int message) {
        this.message = message;
        return this;
    }

    public long getBitRate() {
        return bitRate;
    }

    public StreamingEvent setBitRate(long bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public int getCamera() {
        return camera;
    }

    public StreamingEvent setCamera(int camera) {
        this.camera = camera;
        return this;
    }
}
