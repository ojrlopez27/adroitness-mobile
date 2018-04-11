package edu.cmu.adroitness.client.services.email.control;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.email.model.GmailFilterQueryInputVO;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;

/**
 * Created by sakoju on 12/15/16.
 */

public class GmailController {
    private static GmailController instance;
    protected MessageBroker mMB;
    protected Activity mActivity;
    public Context context;
    public Message message;
    public Handler handler;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    protected String accountName;

    protected GmailController(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( mActivity );
        mMB.subscribe(this);
    }

    public static GmailController getInstance(Activity activity) {
        if (instance == null) {
            instance = new GmailController(activity);
        }
        return instance;
    }
    public void initializeGmail(){
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_INITIALIZE));
    }
    public void subscribe( Activity subscriber ){
        mMB.subscribe(subscriber);
    }

    public void unsubscribe( Activity subscriber ){
        mMB.unsubscribe(subscriber);
    }

    /******************************************* Gmail *****************************************/

    /**
     * Updates latest labels from gmail account (both default as well as user defined labels).
     * Get labels from onEventMainThread(GmailManagerEvent event)
     */
    public void updateLabelListFromGmail()
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_UPDATE_LABEL_LIST_FROM_GMAIL));
    }



    /**
     * FilterMessagesByQuery queries Gmail with search filters
     * @param gmailFilterQueryInputVO
     * (just fill in this object for types of search filters within Gmail
     */
    public void filterMessagesByQuery(GmailFilterQueryInputVO gmailFilterQueryInputVO, Boolean isSendEvent)
    {
        if(gmailFilterQueryInputVO==null)
        {
            gmailFilterQueryInputVO = new GmailFilterQueryInputVO();
            gmailFilterQueryInputVO.setUserId("me");
            gmailFilterQueryInputVO.setFolderName(
                    GmailFiltersValidation.isDefaultGmailFolder(Constants.GMAIL_FOLDERS.get(0)));
            gmailFilterQueryInputVO.setLabelName(
                    GmailFiltersValidation.isDefaultGmailLabel(Constants.GMAIL_DEFAULT_LABELS.get(0)));
            gmailFilterQueryInputVO.setMaxResults("5");
            gmailFilterQueryInputVO.setHasAtttachment(false);
            gmailFilterQueryInputVO.setFileName("*.pdf");
            gmailFilterQueryInputVO.setBeforeDate(new Date(System.currentTimeMillis()+100000000));
        }
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_FILTER_MESSAGES_BY_QUERY)
                .put(Constants.GMAIL_FILTER_QUERY_INPUT_VO, gmailFilterQueryInputVO)
                .put(Constants.GMAIL_ACTIVITY_REQUEST_FLAG, isSendEvent));
    }

    /**
     * Call this method if you want to refresh the Gmail Data
     * Returns in onEventMainThread(GmaiManagerEvent event) method last 10 email messages
     * in following parameters from event:
     * messageList, MessageAdapterList, MimeMessages, Json format of complete Message
     */
    public void getDataFromGmail() {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_DATA_FROM_GMAIL));
    }
    public void getMessage(String messageID)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_MESSAGE)
                .put(Constants.GMAIL_MESSAGE_ID, messageID));
    }

    public void chooseAccount(Activity mActivity) {
        mMB.send(mActivity,
                MBRequest.build(Constants.MSG_CHOOSE_ACCOUNT)
                        .put(Constants.BUNDLE_ACTIVITY_NAME, mActivity));
    }

    public void configAccountName(Activity activity, String accountName) {
        mMB.send(activity,
                MBRequest.build(Constants.MSG_CONFIG_ACCOUNT_NAME)
                        .put(Constants.BUNDLE_ACTIVITY_NAME, activity)
                        .put(Constants.ACCOUNT_NAME, accountName)
                        .put(Constants.ACCOUNT_TYPE, Constants.GOOGLE_ACCOUNT));
        this.accountName = accountName;
    }

    public void checkGooglePlayServicesAvailable() {
        mMB.send(mActivity,
                MBRequest.build(Constants.MSG_GOOGLE_PLAY_AVAILABLE));
    }

    /**
     * Get MimeMessage type (javax.Mail.MimeMessage type) and capture this in onEventMainThread
     * @param messageID
     * Example: MimeMessage mimeMessage = getMimeMessage(message.getId());
     */
    public void getMimeMessage(String messageID)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_MIME_MESSAGE)
        .put(Constants.GMAIL_MESSAGE_ID, messageID));
    }

    /**
     * Get message message content in String format
     * Receive event.getMessageBody() from single message details in onEventMainThread(GmaiManagerEvent event) method
     * @param messageID
     */
    public void getMessageContent(String messageID, Boolean isActivityRequest)
    {
        isActivityRequest = true;
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_MESSAGE_CONTENT)
                .put(Constants.GMAIL_MESSAGE_ID, messageID)
        .put(Constants.GMAIL_ACTIVITY_REQUEST_FLAG,isActivityRequest ));
    }

    /**
     * Receive Json format of single message details in onEventMainThread(GmaiManagerEvent event) method
     * @param messageID
     * @param isListViewItemClick
     */
    public void getJsonEmailMessageDetails(String messageID, Boolean isListViewItemClick)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_JSON_EMAIL_MESSAGE_DETAILS)
                .put(Constants.GMAIL_MESSAGE_ID, messageID)
        .put(Constants.GMAIL_LISTVIEW_FLAG, isListViewItemClick));
    }

    //send("me", "to@jjj.com, from@gmail.com, "notification", "hi, thie is inform...");

    /**
     * Send email
     * @param gmailMessageVO
     * Add any emails to "ToEmail or ccEmail" in GmailMessageVO
     * (Comma seperated String format email IDs or a single email ID)
     *                           eg: "abc@gmail.com,def@gmail.com" or "abc#gmail.com"
     * You can add attachment in gmailMessageVO
     * Example: sendMessage(gmailMessageVO);
     * GmailMessageVO: must contain: "abc@xyz.com, def@xyz.com", "Test email with Attachment: Adroitness",
     * "Hi this is test email with attachment" )
     * DISCLAIMER: Does not support attachment. There is seperate method to handle send new email with attachment.
     */
    public void sendMessage(GmailMessageVO gmailMessageVO)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_SEND_EMAIL_MESSAGE)
                .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));
    }

    /**
     * send an email with attachments
     * @param gmailMessageVO
     * Add any emails to "ToEmail or ccEmail" in GmailMessageVO
     * (Comma seperated String format email IDs or a single email ID)
     *                           eg: "abc@gmail.com,def@gmail.com" or "abc#gmail.com"
     * You can add attachment in gmailMessageVO
     * Example: sendMessageWithAttachment(gmailMessageVO);
     * GmailMessageVO "abc@xyz.com, def@xyz.com", "Test email with Attachment: Adroitness",
     * "Hi this is test email with attachment", new File(fileLocation+"/filename.extension")
     */
    public void sendMessageWithAttachment( GmailMessageVO gmailMessageVO)
    {
        if(gmailMessageVO.getAttachedFile()!=null) {
            mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_SEND_EMAIL_MESSAGE_WITH_ATTACHMENT)
                    .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));
        }
    }

    /***
     * Reply to an email
     * @param gmailMessageVO
     * Add any emails to "ToEmail or ccEmail" in GmailMessageVO
     * (Comma seperated String format email IDs or a single email ID)
     *                           eg: "abc@gmail.com,def@gmail.com" or "abc#gmail.com"
     * Example: replyToEmail(gmaiilMessageVO);
     * DISCLAIMER: do not add an attachment, not supported in reply
     */
    public void replyToEmail(GmailMessageVO gmailMessageVO)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_REPLY)
                .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));
    }

    /***
     * Forward an email
     * @param gmailMessageVO
     * Add any emails to "ToEmail or ccEmail" in GmailMessageVO
     * (Comma seperated String format email IDs or a single email ID)
     *                           eg: "abc@gmail.com,def@gmail.com" or "abc#gmail.com"
     * Example: forwardEmail(gmaiilMessageVO);
     * DISCLAIMER: do not add an attachment, not supported in reply
     */
    public void forwardEmail(GmailMessageVO gmailMessageVO)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_FORWARD)
                .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));
    }

    /**
     * get attachments in a single email and Save to phone
     * @param messageID
     * @param saveToPhone
     * Example: getAttachments(message.getId(), new Boolean(true));
     */
    public void getAttachments(String messageID, Boolean saveToPhone)
    {
        mMB.send(GmailController.this, MBRequest.build(Constants.MSG_GMAIL_GET_ATTACHMENTS)
                .put(Constants.GMAIL_MESSAGE_ID, messageID)
                .put(Constants.GMAIL_SAVE_TO_PHONE_FLAG, saveToPhone));
    }


    /**
     * Examples from here for Gmail API calls
     *
     */

    /**
     * Sets default GmailFiler Query:
     * this is an example to set Gmail Filter before calling filterMessagesByQuery(GmailFilterQueryVO obj);
     */
    public void setGmailFilterQueryAndSendFilterQuery()
    {
        GmailFilterQueryInputVO gmailFilterQueryInputVO = new GmailFilterQueryInputVO();
        for(String value : Constants.GMAIL_FOLDERS.values()) {
            if (value.equals("INBOX")) {
                gmailFilterQueryInputVO.setFolderName(value);
            }
        }
        for(String value : Constants.GMAIL_DEFAULT_LABELS.values()) {
            if (value.equals("IMPORTANT")) {
                gmailFilterQueryInputVO.setLabelName(value);
            }
        }
        Date beforeDate = new Date(System.currentTimeMillis());
        gmailFilterQueryInputVO.setBeforeDate(beforeDate);
        gmailFilterQueryInputVO.setStarred(new Boolean(true));
        filterMessagesByQuery(gmailFilterQueryInputVO, new Boolean(true));
    }


}
