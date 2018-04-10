package edu.cmu.adroitness.comm.generic.control.adapters;

import javax.mail.Message;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.email.control.IMAPManagerService;

/**
 * Created by sakoju on 8/9/16.
 */
public class ImapAdapter extends ChannelAdapter {
    private static ImapAdapter instance;

    private ImapAdapter() {
        super();
    }

    public static ImapAdapter getInstance()
    {
        if(instance == null)
        {
            instance = new ImapAdapter();
        }
        return instance;
    }

    public void checkInBox( MBRequest request){
        mResourceLocator.addRequest( IMAPManagerService.class, "checkInBox");
    }

    public void parseContent( MBRequest mbRequest){
        mResourceLocator.addRequest( IMAPManagerService.class, "parseContent",
                (Message) mbRequest.get(Constants.IMAP_MESSAGE));
     }

    public void parseSender( MBRequest mbRequest){
        mResourceLocator.addRequest( IMAPManagerService.class, "parseSender",
                ((String) mbRequest.get(Constants.RAW_CONTENT)));
     }

    public void closeInbox( MBRequest request){
        mResourceLocator.addRequest( IMAPManagerService.class, "closeInbox");
     }

    public void markAllRead( MBRequest request){
        mResourceLocator.addRequest( IMAPManagerService.class,"markAllRead");
     }

    public void getUnReadNum( MBRequest request){
        mResourceLocator.addRequest( IMAPManagerService.class, "getUnReadNum");
    }

    public void getMsg( MBRequest mbRequest){
        mResourceLocator.addRequest( IMAPManagerService.class, "getMessage",
                ((int) mbRequest.get(Constants.IMAP_MESSAGE_ORDER)));
     }

    public void searchContent( MBRequest mbRequest){
        mResourceLocator.addRequest( IMAPManagerService.class, "searchContent",
                ((String) mbRequest.get(Constants.IMAP_QUERY)));
     }
}
