package edu.cmu.adroitness.client.services.email.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by sakoju on 8/8/16.
 */
public class ImapVO extends DataObject {
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String host;
    private String password;
    private String userName;
}
