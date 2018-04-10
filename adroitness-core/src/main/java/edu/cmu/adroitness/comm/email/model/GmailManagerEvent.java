package edu.cmu.adroitness.comm.email.model;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Profile;
import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;

/**
 * Created by sakoju on 8/8/16.
 */
public class GmailManagerEvent extends BaseEvent {

    private String notification;
    private boolean isError = false;
    private Object params;
    private List<GmailMessageVO> emails;
    private List<GmailMessageVO> allEmails;
    private List<GmailMessageVO> newEmails = new ArrayList<>();
    private List<GmailMessageVO> sentEmails = new ArrayList<>();
    private List<GmailMessageVO> repliedEmails = new ArrayList<>();
    private List<GmailMessageVO> forwardedEmail = new ArrayList<>();


    private String messageString="";
    private List<String> labels;
    private List<Message> messageList;
    private Integer unreadCounter;
    private String messageBody;
    private Label label;

    private Profile userProfile;

    private GmailMessageVO gmailMessageVO;
    private String[]  messageAdapterList;
    //private String jsonMessageString ="";
    private Message messageEmail = null;
    private MimeMessage email =null;
    private String filterQuery="";
    private String userID = "me";

    private String filesDirectory ="";

    public GmailMessageVO getGmailMessageVO() {
        return gmailMessageVO;
    }

    public GmailManagerEvent setGmailMessageVO(GmailMessageVO gmailMessageVO) {
        this.gmailMessageVO = gmailMessageVO;
        return this;
    }


    public String getNotification() {
        return notification;
    }

    public GmailManagerEvent setNotification(String notification) {
        this.notification = notification;
        return this;
    }

    public boolean isError() {
        return isError;
    }

    public GmailManagerEvent setError(boolean error) {
        isError = error;
        return this;
    }

    public List<GmailMessageVO> getEmails() {
        return emails;
    }

    public GmailManagerEvent setEmails(List<GmailMessageVO> emails) {
        this.emails = emails;
        return this;
    }

    public List<GmailMessageVO> getAllEmails() {
        return allEmails;
    }

    public GmailManagerEvent setAllEmails(List<GmailMessageVO> allEmails) {
        this.allEmails = allEmails;
        return this;
    }

    public List<GmailMessageVO> getNewEmails() {
        return newEmails;
    }

    public GmailManagerEvent setNewEmails(List<GmailMessageVO> newEmails) {
        this.newEmails = newEmails;
        return this;
    }

    public List<GmailMessageVO> getSentEmails() {
        return sentEmails;
    }

    public GmailManagerEvent setSentEmails(List<GmailMessageVO> sentEmails) {
        this.sentEmails= sentEmails;
        return this;
    }

    public List<GmailMessageVO> getRepliedEmails() {
        return repliedEmails;
    }

    public GmailManagerEvent setRepliedEmails(List<GmailMessageVO> repliedEmails) {
        this.repliedEmails = repliedEmails;
        return this;
    }

    public List<GmailMessageVO> getForwardedEmail() {
        return forwardedEmail;
    }

    public GmailManagerEvent setForwardedEmail(List<GmailMessageVO> forwardedEmail) {
        this.forwardedEmail = forwardedEmail;
        return this;
    }

    public Integer getUnreadCounter() {
        return unreadCounter;
    }

    public GmailManagerEvent setUnreadCounter(Integer unreadCounter) {
        this.unreadCounter = unreadCounter;
        return this;
    }



    public String getUserID() {
        return userID;
    }

    public GmailManagerEvent setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getFilterQuery() {
        return filterQuery;
    }

    public GmailManagerEvent setFilterQuery(String filterQuery) {
        this.filterQuery = filterQuery;
        return this;
    }

    public Object getParams() {
        return params;
    }

    public GmailManagerEvent setParams(Object o) {
        this.params = o;
        return this;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public GmailManagerEvent setMessageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    public String[] getMessageAdapterList() {
        return messageAdapterList;
    }

    public GmailManagerEvent setMessageAdapterList(String[] messageAdapterList) {
        this.messageAdapterList = messageAdapterList;
        return this;
    }

    public MimeMessage getEmail() {
        return email;
    }

    public GmailManagerEvent setEmail(MimeMessage email) {
        this.email = email;
        return this;
    }

    public Message getMessageEmail() {
        return messageEmail;
    }

    public GmailManagerEvent setMessageEmail(Message messageEmail) {
        this.messageEmail = messageEmail;
        return this;
    }

    public String getMessageString() {
        return messageString;
    }

    public GmailManagerEvent setMessageString(String messageString) {
        this.messageString = messageString;
        return this;
    }

    protected GmailManagerEvent() {
        super();
    }

    protected GmailManagerEvent(Integer mbRequestId) {
        super(mbRequestId);
    }
    public static GmailManagerEvent build()
    {
        return new GmailManagerEvent();
    }

    public List<String> getLabels() {
        return labels;
    }

    public GmailManagerEvent setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public GmailManagerEvent setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        return this;
    }

    public Label getLabel() {
        return label;
    }

    public GmailManagerEvent setLabel(Label label) {
        this.label = label;
        return this;
    }

    public Profile getUserProfile() {
        return userProfile;
    }

    public GmailManagerEvent setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getFilesDirectory() {
        return filesDirectory;
    }

    public GmailManagerEvent setFilesDirectory(String filesDirectory) {
        this.filesDirectory = filesDirectory;
        return this;
    }

}
