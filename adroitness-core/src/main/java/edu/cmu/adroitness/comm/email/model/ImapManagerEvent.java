package edu.cmu.adroitness.comm.email.model;

import android.content.Context;
import android.os.Handler;
import javax.mail.Message;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.email.model.ImapVO;

/**
 * Created by sakoju on 8/8/16.
 */
public class ImapManagerEvent extends BaseEvent {

    private String messageString="";
    private Context contect;
    private Message message;

    public ImapVO getImapVO() {
        return imapVO;
    }

    public void setImapVO(ImapVO imapVO) {
        this.imapVO = imapVO;
    }

    private ImapVO imapVO;


    public Context getContect() {
        return contect;
    }

    public void setContect(Context contect) {
        this.contect = contect;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;


    public String getMessageString() {
        return messageString;
    }

    public ImapManagerEvent setMessageString(String messageString) {
        this.messageString = messageString;
        return this;
    }

    protected ImapManagerEvent() {
        super();
    }

    protected ImapManagerEvent(Integer mbRequestId) {
        super(mbRequestId);
    }
    public static ImapManagerEvent build()
    {
        return new ImapManagerEvent();
    }
}
