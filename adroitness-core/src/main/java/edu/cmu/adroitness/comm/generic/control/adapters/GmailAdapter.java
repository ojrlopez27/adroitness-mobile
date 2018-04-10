package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.email.control.GmailManagerService;
import edu.cmu.adroitness.client.services.email.model.GmailFilterQueryInputVO;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;

/**
 * Created by sakoju on 8/9/16.
 */
public class GmailAdapter extends ChannelAdapter {
    private static GmailAdapter instance;


    private GmailAdapter() {
        super();
    }

    public static GmailAdapter getInstance()
    {
        if(instance == null)
        {
            instance = new GmailAdapter();
        }
        return instance;
    }

    public void getUnreadNum( MBRequest request){
        mResourceLocator.addRequest( GmailManagerService.class, "getUnReadNum");
    }


    public void getSender( MBRequest mbRequest){
        mResourceLocator.addRequest( GmailManagerService.class, "getSender",
                ((String) mbRequest.get(Constants.GMAIL_MESSAGE_ID)));
    }

    public void initializeGmail( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "initializeGmail");
    }

    public void updateLabelListFromGmail( MBRequest mbRequest)
    {
        mResourceLocator.addRequest( GmailManagerService.class, "updateLabelListFromGmail");
    }

    public void getDataFromGmail( MBRequest mbRequest)
    {
        mResourceLocator.addRequest( GmailManagerService.class, "getDataFromGmail");
    }

    public void filterMessagesByQuery( MBRequest mbRequest)
    {
        mResourceLocator.addRequest( GmailManagerService.class, "filterMessagesByQuery",
        ((GmailFilterQueryInputVO) mbRequest.get(Constants.GMAIL_FILTER_QUERY_INPUT_VO)),
                mbRequest.get(Constants.GMAIL_ACTIVITY_REQUEST_FLAG));

    }

    public void getJsonEmailMessageDetails( MBRequest mbRequest)
    {
        mResourceLocator.addRequest( GmailManagerService.class, "getJsonEmailMessageDetails",
                ((String) mbRequest.get(Constants.GMAIL_MESSAGE_ID)),
                ((Boolean) mbRequest.get(Constants.GMAIL_LISTVIEW_FLAG)));
    }

    public void getMessageContent(final MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "getMessageContent",
                ((String) mbRequest.get(Constants.GMAIL_MESSAGE_ID)),
                (Boolean) mbRequest.get(Constants.GMAIL_ACTIVITY_REQUEST_FLAG));
    }

    public void getMessage( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "getMessage",
                ((String) mbRequest.get(Constants.GMAIL_MESSAGE_ID)));
    }

    public void getMimeMessage( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "getMimeMessage",
                ((String) mbRequest.get(Constants.GMAIL_MESSAGE_ID)));
    }

    public void sendMessage( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "sendMessage",
                ((GmailMessageVO) mbRequest.get(Constants.GMAIL_MESSAGE_VO)));
    }

    public void sendMessageWithAttachment( MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "sendMessageWithAttachment",
                ((GmailMessageVO) mbRequest.get(Constants.GMAIL_MESSAGE_VO)));
    }

    public void getAttachments(MBRequest mbRequest)
    {
        try {
            mResourceLocator.addRequest(GmailManagerService.class,
                    "getAttachments", (String) mbRequest.get(Constants.GMAIL_MESSAGE_ID),
                            (Boolean)mbRequest.get(Constants.GMAIL_SAVE_TO_PHONE_FLAG));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void replyToEmail(MBRequest mbRequest)
    {
            mResourceLocator.addRequest(GmailManagerService.class, "replyToEmail",
                    ((GmailMessageVO) mbRequest.get(Constants.GMAIL_MESSAGE_VO)));
    }
    public void forwardEmail(MBRequest mbRequest)
    {
            mResourceLocator.addRequest(GmailManagerService.class, "forwardEmail" ,
                    ((GmailMessageVO) mbRequest.get(Constants.GMAIL_MESSAGE_VO)));
    }

    public void getLabel(MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "getLabel" ,
                ((String) mbRequest.get(Constants.GMAIL_LABEL)));
    }
    public void getUserProfile(MBRequest mbRequest)
    {
        mResourceLocator.addRequest(GmailManagerService.class, "getUserProfile");
    }

}