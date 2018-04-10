package edu.cmu.adroitness.comm.generic.model;

/**
 * Created by oscarr on 11/23/16.
 */

public class MiddlewareNotificationEvent extends BaseEvent {

    private boolean isMiddlewareReady;
    private String sourceName = "";
    private String message = "";

    private MiddlewareNotificationEvent(){ super(); }
    private MiddlewareNotificationEvent(int mbRequestId){ super( mbRequestId); }

    public static MiddlewareNotificationEvent build(){
        return new MiddlewareNotificationEvent();
    }
    public static MiddlewareNotificationEvent build(int mbRequestId){
        return new MiddlewareNotificationEvent(mbRequestId);
    }

    public boolean isMiddlewareReady() {
        return isMiddlewareReady;
    }

    public MiddlewareNotificationEvent setMiddlewareReady(boolean middlewareReady) {
        isMiddlewareReady = middlewareReady;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MiddlewareNotificationEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getSourceName() {
        return sourceName;
    }

    public MiddlewareNotificationEvent setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }
}
