package edu.cmu.adroitness.client.services.email.model;

import java.io.File;
import java.util.Date;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by sakoju on 3/3/17.
 */

public class GmailMessageVO extends DataObject implements Comparable<GmailMessageVO> {

    private String subject;
    private String fromEmail;
    private String toEmail;
    private String threadID;
    private String messageID;
    private String messageContent;
    private Boolean hasAttachment;
    private Date emailDate;
    private String jsonString="";
    private File attachedFile;
    Boolean replyAllFlag = false;
    private String ccEmail;

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public File getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(File attachedFile) {
        this.attachedFile = attachedFile;
    }

    public Boolean getReplyAllFlag() {
        return replyAllFlag;
    }

    public void setReplyAllFlag(Boolean replyAllFlag) {
        this.replyAllFlag = replyAllFlag;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getThreadID() {
        return threadID;
    }

    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    @Override
    public int compareTo(GmailMessageVO another) {
        if ( compare(messageID, another.messageID) && compare(subject, another.subject)
                && compare(fromEmail, another.fromEmail) && compare(toEmail, another.toEmail)
                && compare(threadID, another.threadID) && compare(messageContent, another.messageContent)
                && compare(hasAttachment, another.hasAttachment) && compare(emailDate, another.emailDate)){
            return 0;
        }
        return -1;
    }

    private boolean compare(Object one, Object two){
        if( one == null && two == null ){
            return true;
        }else if( one != null && two != null && one.equals(two) ){
            return true;
        }
        return false;
    }
}
