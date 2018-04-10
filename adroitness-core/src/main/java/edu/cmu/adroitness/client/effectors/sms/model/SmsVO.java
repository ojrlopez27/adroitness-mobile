package edu.cmu.adroitness.client.effectors.sms.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by oscarr on 9/29/15.
 */
public class SmsVO extends DataObject {

    private String phoneNumber;
    private String message;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
