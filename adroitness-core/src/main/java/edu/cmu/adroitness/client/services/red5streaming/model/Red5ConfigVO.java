package edu.cmu.adroitness.client.services.red5streaming.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by sakoju on 7/12/16.
 */

/***
 * The Red5ConfigVO class maintains all required, editable settings of Red5 for Red5 Server.
 * Red5 Streaming server utilizes these features.
 */
public class Red5ConfigVO extends DataObject {
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public Boolean getAudio() {
        return audio;
    }

    public void setAudio(Boolean audio) {
        this.audio = audio;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }


    private String app;
    private int port;
    private String name;
    private int bitrate;
    private Boolean audio;
    private Boolean video;
    private String resolution;
}
