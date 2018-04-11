package com.yahoo.inmind.testing.middleware;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.email.control.GmailFiltersValidation;
import edu.cmu.adroitness.client.services.email.control.GmailManagerService;
import edu.cmu.adroitness.client.services.email.model.GmailFilterQueryInputVO;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;

//import org.mockito.ArgumentCaptor;

/**
 * Created by sakoju on 3/15/17.
 */


@SmallTest
public class GmailAdapterTest {
    protected static final String TAG = "Gmailtest";
    protected static Context context;
    protected static MessageBroker mb;
    protected static ResourceLocator locator;
    protected static GmailManagerService gmailManagerService;
    protected static GmailManagerEvent eventVO;
    protected static HashMap attributes;
    protected static String accountName = "inmind.yahoo.test@gmail.com";
    protected static String defaultUserID = "me";
    protected static List<GmailMessageVO> gmailMessageVOList;
    protected static String messageID="";

    @BeforeClass
    public static void setup() {
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add(Constants.ADD_SERVICE_GMAIL_SERVICE);
        mb = MessageBroker.getInstance(context, services);
        locator = ResourceLocator.getInstance(context);
        mb.subscribe(context);
        UtilServiceAPIs.initializeCredentials(context, accountName);
        ResourceLocator.getExistingInstance().setAccount(Constants.GOOGLE_ACCOUNT, accountName);
        do {
            gmailManagerService = locator.lookupService(GmailManagerService.class);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (gmailManagerService == null);
        gmailManagerService.initializeGmail();
        filterMessageByQuery();
        //com.jayway.awaitility.Awaitility.await().atMost(30, TimeUnit.SECONDS);
    }

    public static void filterMessageByQuery() {
        gmailMessageVOList = new ArrayList<>();
        GmailFilterQueryInputVO gmailFilterQueryInputVO = new GmailFilterQueryInputVO();
        gmailFilterQueryInputVO.setUserId("me");
        gmailFilterQueryInputVO.setFolderName(
                GmailFiltersValidation.isDefaultGmailFolder(Constants.GMAIL_FOLDERS.get(0)));
        gmailFilterQueryInputVO.setLabelName(
                GmailFiltersValidation.isDefaultGmailLabel(Constants.GMAIL_DEFAULT_LABELS.get(0)));
        gmailFilterQueryInputVO.setMaxResults("10");
        gmailFilterQueryInputVO.setHasAtttachment(false);
        //gmailFilterQueryInputVO.setFileName("*.pdf");
        gmailFilterQueryInputVO.setBeforeDate((new Date(System.currentTimeMillis() + 100000000)));
        try {
            gmailManagerService.filterMessagesByQuery(gmailFilterQueryInputVO, new Boolean(true));
            //com.jayway.awaitility.Awaitility.await().pollDelay(10, TimeUnit.SECONDS).until(
                   // ()-> gmailManagerService.getGmailMessageVOList().size()>0);
            //messageID = gmailMessageVOList.get(0).getMessageID();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //org.junit.Assert.assertTrue(gmailMessageVOList.size() > 0);
    }

    @Test
    public void sendMessage() {

        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setToEmail("sushma.ananda13@gmail.com");
        gmailMessageVO.setSubject("Unit test");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageContent("this is a test email sent from inmind app.");

        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_SEND_EMAIL_MESSAGE)
                    .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));
        /*ArgumentCaptor<GmailManagerEvent> gmailManagerEventArgumentCaptor = ArgumentCaptor.forClass(GmailManagerEvent.class);
        GmailManagerEvent gmailManagerEvent = gmailManagerEventArgumentCaptor.capture();
        List<GmailManagerEvent> gmailManagerEventList = gmailManagerEventArgumentCaptor.getAllValues();*/
        //org.junit.Assert.assertTrue(gmailManagerEventList.size()>0);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GmailManagerEvent event)
    {
        Log.d("Gmail","Gmail Event");
        if(event.getNotification()!=null)
        {
            if(event.getNotification().equals(Constants.GMAIL_AFTER_SEND_EMAIL))
            {
                List<GmailMessageVO> sentEmails = event.getSentEmails();
                org.junit.Assert.assertTrue(sentEmails.size() > 0);
            }
            if(event.getNotification().equals(Constants.MSG_GMAIL_FORWARD))
            {
                List<GmailMessageVO> forward = event.getSentEmails();
                org.junit.Assert.assertTrue(forward.size() > 0);
            }
            if(event.getNotification().equals(Constants.GMAIL_AFTER_REPLY_EMAIL))
            {
                List<GmailMessageVO> repliedEmails = event.getRepliedEmails();
                org.junit.Assert.assertTrue(repliedEmails.size() > 0);
            }
        }
    }

    @Test
    public void forwardMessage() {
        gmailMessageVOList = new ArrayList<>();
        List<GmailMessageVO> forward = new ArrayList<>();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setToEmail("sushma.ananda13@gmail.com");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageID(messageID);
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_FORWARD)
                .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));

    }

    @Test
    public void replyToEmail() {
        gmailMessageVOList = new ArrayList<>();
        gmailManagerService.getGmailMessageVOList();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        //gmailMessageVO.setToEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageID(messageID);
        gmailMessageVO.setMessageContent("reply test mail.");
        gmailMessageVO.setReplyAllFlag(new Boolean(false));

        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_REPLY)
                    .put(Constants.GMAIL_MESSAGE_VO, gmailMessageVO));

    }

    @Test
    public void updateLabels() {
        List<String> labels = new ArrayList<>();
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_UPDATE_LABEL_LIST_FROM_GMAIL));

        //org.junit.Assert.assertTrue(labels.size() > 0);
    }

    @Test
    public void getUnreadGmailNum() {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_UNREAD_NUM));
        //org.junit.Assert.assertTrue(count > 0);
    }

    @Test
    public void getSender()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_SENDER)
                .put(Constants.GMAIL_MESSAGE_ID, messageID));
       // org.junit.Assert.assertNotNull(sender);
    }

    @Test
    public void getMimeMessage()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_MIME_MESSAGE)
                .put(Constants.GMAIL_MESSAGE_ID, messageID));
        //org.junit.Assert.assertNotNull(mimeMessage);
    }

    @Test
    public void getJson()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_JSON_EMAIL_MESSAGE_DETAILS)
                .put(Constants.GMAIL_MESSAGE_ID, messageID)
        .put(Constants.GMAIL_LISTVIEW_FLAG, new Boolean(false)));
        //org.junit.Assert.assertNotNull(gmailMessageVO);
    }

    @Test
    public void getMessageContent()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_MESSAGE_CONTENT)
                .put(Constants.GMAIL_MESSAGE_ID, messageID)
        .put(Constants.GMAIL_ACTIVITY_REQUEST_FLAG, new Boolean(false)));
        //org.junit.Assert.assertNotNull(gmailManagerService.getBodyString(messageID));
       /* ArgumentCaptor<GmailManagerEvent> gmailManagerEventArgumentCaptor = ArgumentCaptor.forClass(GmailManagerEvent.class);
        GmailManagerEvent gmailManagerEvent = gmailManagerEventArgumentCaptor.capture();
        org.mockito.Mockito.verify(this).onEventMainThread(gmailManagerEventArgumentCaptor.capture());
        String messageContent = gmailManagerEventArgumentCaptor.getValue().getMessageBody();*/
        //org.junit.Assert.assertTrue(!messageContent.equals(""));
    }

    @Test
    public void getLabel()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_LABEL)
                .put(Constants.GMAIL_LABEL, "CATEGORY_SOCIAL"));
        /*org.junit.Assert.assertNotNull(label);
        org.junit.Assert.assertNotNull(label.getMessagesTotal());
        org.junit.Assert.assertNotNull(label.getName());
        org.junit.Assert.assertNotNull(label.getMessagesUnread());
        org.junit.Assert.assertNotNull(label.getThreadsTotal());
        org.junit.Assert.assertNotNull(label.getThreadsUnread());*/

    }

    @Test
    public void getProfile()
    {
        mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_USER_PROFILE));
       /* org.junit.Assert.assertNotNull(profile);
        org.junit.Assert.assertNotNull(profile.getMessagesTotal());
        org.junit.Assert.assertNotNull(profile.getThreadsTotal());
        org.junit.Assert.assertNotNull(profile.getEmailAddress());*/

    }

    @Test
    public void getAttachments()
    {
            mb.send(context, MBRequest.build(Constants.MSG_GMAIL_GET_ATTACHMENTS)
                    .put(Constants.GMAIL_MESSAGE_ID, messageID)
            .put(Constants.GMAIL_SAVE_TO_PHONE_FLAG, new Boolean(true)));

        /*org.junit.Assert.assertNotNull(gmailManagerService.getSavedDirectoryPath());
        org.junit.Assert.assertTrue(gmailManagerService.getAttachmentSaved());*/
        /*if(gmailManagerService.getAttachmentSaved()) {
            File file = new File(gmailManagerService.getSavedDirectoryPath());
            if (file.exists() && file.isDirectory()) {
                org.junit.Assert.assertTrue(file.listFiles().length > 0);
            }
        }*/
    }

}
