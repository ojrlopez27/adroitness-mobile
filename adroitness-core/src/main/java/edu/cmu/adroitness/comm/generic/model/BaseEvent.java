package edu.cmu.adroitness.comm.generic.model;

/**
 * Created by oscarr on 1/22/15.
 */
public class BaseEvent {
    private Integer mbRequestId;
    private Object subscriber;

    protected BaseEvent(){}

    protected BaseEvent(Integer mbRequestId) {
        this.mbRequestId = mbRequestId;
    }

    public static BaseEvent build(){
        return new BaseEvent();
    }

    public static BaseEvent build(Integer mbRequestId){
        return new BaseEvent(mbRequestId);
    }

    public Integer getMbRequestId() {
        return mbRequestId;
    }

    public <T> T setMbRequestId(Integer mbRequestId) {
        this.mbRequestId = mbRequestId;
        return (T)this;
    }


    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }
}
