package edu.cmu.adroitness.comm.sms.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 6/21/16.
 */
public class IncomingSmsEvent extends BaseEvent {
    private String message;
    private String phoneNumber;

    private IncomingSmsEvent(){ super(); }
    public static IncomingSmsEvent build(){
        return new IncomingSmsEvent();
    }

    public String getMessage() {
        return message;
    }

    public IncomingSmsEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessages() {
        return message;
    }

    public IncomingSmsEvent setMessages(String message) {
        this.message = message;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public IncomingSmsEvent setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
