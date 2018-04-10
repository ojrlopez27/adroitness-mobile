package edu.cmu.adroitness.comm.red5streaming.model;

import android.hardware.Camera;
import android.view.SurfaceView;

import com.red5pro.streaming.R5Stream;
import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;


/**
 * Created by sakoju on 7/11/16.
 */
public class Red5StreamingEvent extends BaseEvent {

    private boolean result;
    private R5Stream stream;
    private Red5ConfigVO config;
    private Camera camera;
    private SurfaceView surfaceView;
    private String operation;

    public R5Stream.R5Stats getR5Stats() {
        return r5Stats;
    }

    public Red5StreamingEvent setR5Stats(R5Stream.R5Stats r5Stats) {
        this.r5Stats = r5Stats;
        return this;
    }

    private R5Stream.R5Stats r5Stats;

    public String getRed5StreamingServerIPAddress() {
        return red5StreamingServerIPAddress;
    }

    public Red5StreamingEvent setRed5StreamingServerIPAddress(String red5StreamingServerIPAddress) {
        this.red5StreamingServerIPAddress = red5StreamingServerIPAddress;
        return this;
    }

    private String red5StreamingServerIPAddress="";

    public String getRed5StreamingError() {
        return red5StreamingError;
    }

    public Red5StreamingEvent setRed5StreamingError(String red5StreamingError) {
        this.red5StreamingError = red5StreamingError;
        return this;
    }

    private String red5StreamingError;

    public String getServiceStatus() {
        return serviceStatus;
    }

    public Red5StreamingEvent setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
        return this;
    }

    private String serviceStatus;
    public Red5StreamingEvent() {
        super();
    }

    public Red5StreamingEvent(Integer mbRequestId) {
        super(mbRequestId);
    }

    public static Red5StreamingEvent build()
    {
        return new Red5StreamingEvent();
    }

    public boolean getResult()
    {
        return result;
    }

    public Red5StreamingEvent setResult(boolean result)
    {
        this.result = result;
        return this;
    }

    public Red5StreamingEvent setStreamConfig(Red5ConfigVO c)
    {
        this.config = c;
        return this;
    }

    public Red5StreamingEvent setStreamCamera(Camera camera)
    {
        this.camera = camera;
        return this;
    }

    public Red5StreamingEvent setStreamSurface(SurfaceView sv)
    {
        this.surfaceView = sv;
        return this;
    }
    public Red5StreamingEvent setOperation(String op)
    {
        this.operation = op;
        return this;
    }


}
