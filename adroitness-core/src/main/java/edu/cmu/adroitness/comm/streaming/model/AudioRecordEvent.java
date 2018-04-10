package edu.cmu.adroitness.comm.streaming.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 2/27/15.
 */
public class AudioRecordEvent extends BaseEvent{
    private byte[] buffer;
    private int sizeInBytes;
    private int minBufferSize;
    private int sampleRate;
    private int channelConfig;
    private int audioEncoding;
    private boolean newConfiguration = false;
    private String errorMessage = null;

    public AudioRecordEvent() {
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }


    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public void setMinBufferSize(int minBufferSize) {
        if (minBufferSize % 2 == 1)
            minBufferSize++;
        this.minBufferSize = minBufferSize;
    }

    public int getMinBufferSize() {
        return minBufferSize;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
    }

    public int getAudioEncoding() {
        return audioEncoding;
    }

    public void setAudioEncoding(int audioEncoding) {
        this.audioEncoding = audioEncoding;
    }

    public boolean isNewConfiguration() {
        return newConfiguration;
    }

    public void setNewConfiguration(boolean newConfiguration) {
        this.newConfiguration = newConfiguration;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
