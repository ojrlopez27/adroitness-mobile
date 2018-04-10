package com.yahoo.inmind.testing.middleware;


import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;
import com.google.api.services.gmail.model.Profile;
import com.yahoo.inmind.comm.email.model.GmailManagerEvent;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.commons.control.UtilServiceAPIs;
import com.yahoo.inmind.services.email.control.GmailFiltersValidation;
import com.yahoo.inmind.services.email.control.GmailManagerService;
import com.yahoo.inmind.services.email.model.GmailFilterQueryInputVO;
import com.yahoo.inmind.services.email.model.GmailMessageVO;

/**
 * Created by sakoju on 3/13/17.
 */

@SmallTest
public class GmailTest {
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
    }

    @Test
    public void sendMessage() {
        List<GmailMessageVO> sentEmails = new ArrayList<>();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setToEmail("sushma.ananda13@gmail.com");
        gmailMessageVO.setSubject("Unit test");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageContent("this is a test email sent from inmind app.");
        try {
            gmailManagerService.sendMessage(gmailMessageVO);
           // sentEmails = gmailManagerService.getSentEmails();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //org.junit.Assert.assertTrue(sentEmails.size() > 0);
    }

    // @Test
    public void sendMessageWithAttachment() {
        List<GmailMessageVO> sentEmails = new ArrayList<>();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setToEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setSubject("Unit test");
        gmailMessageVO.setMessageContent("this is a test email with attachment pittsburgh.jpg sent from inmind app.");
        //file creation from resources for testing
        InputStream inputStream = InstrumentationRegistry.getInstrumentation().getContext().getResources().openRawResource(com.yahoo.inmind.adroitness.R.raw.pittsburgh);
        try {

            File file = File.createTempFile("pittsburgh", ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }
            gmailMessageVO.setHasAttachment(true);
            gmailMessageVO.setAttachedFile(file);
            gmailManagerService.sendMessageWithAttachment(gmailMessageVO);
            //sentEmails = gmailManagerService.getSentEmails();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //org.junit.Assert.assertTrue(sentEmails.size() > 0);

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
            gmailMessageVOList = gmailManagerService.getGmailMessageVOList();
            messageID = gmailMessageVOList.get(0).getMessageID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //org.junit.Assert.assertTrue(gmailMessageVOList.size() > 0);
    }


    @Test
    public void forwardMessage() {
        gmailMessageVOList = new ArrayList<>();
        gmailManagerService.getGmailMessageVOList();
        List<GmailMessageVO> forward = new ArrayList<>();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setToEmail("sushma.ananda13@gmail.com");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageID(messageID);
        try {
            gmailManagerService.forwardEmail(gmailMessageVO);
            //forward = gmailManagerService.getForwardedEmails();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //org.junit.Assert.assertTrue(forward.size() > 0);
    }

    @Test
    public void replyToEmail() {
        gmailMessageVOList = new ArrayList<>();
        gmailManagerService.getGmailMessageVOList();
        List<GmailMessageVO> repliedEmails = new ArrayList<>();
        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        //gmailMessageVO.setToEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setFromEmail("inmind.yahoo.test@gmail.com");
        gmailMessageVO.setMessageID(messageID);
        gmailMessageVO.setMessageContent("reply test mail.");
        gmailMessageVO.setReplyAllFlag(new Boolean(false));
        try {
            gmailManagerService.replyToEmail(gmailMessageVO);
            //repliedEmails = gmailManagerService.getRepliedEmails();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        org.junit.Assert.assertTrue(repliedEmails.size() > 0);
    }

    @Test
    public void updateLabels() {
        List<String> labels = new ArrayList<>();
        gmailManagerService.updateLabelListFromGmail();
        //labels = gmailManagerService.getLabels();

        //org.junit.Assert.assertTrue(labels.size() > 0);
    }

    @Test
    public void getUnreadGmailNum() {
        Integer count = gmailManagerService.getUnreadGmailNum();
        org.junit.Assert.assertTrue(count > 0);
    }

    @Test
    public void getSender()
    {
        String sender =gmailManagerService.getSenderString(messageID);
        org.junit.Assert.assertNotNull(sender);
    }

    @Test
    public void getMimeMessage()
    {
        try {
            MimeMessage mimeMessage = gmailManagerService.getMimeMessage(messageID);
            org.junit.Assert.assertNotNull(mimeMessage);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(MessagingException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void getJson()
    {
        GmailMessageVO gmailMessageVO =gmailManagerService.getJsonEmailMessageDetails(messageID, new Boolean(false));
        org.junit.Assert.assertNotNull(gmailMessageVO);
    }

    @Test
    public void getMessageContent()
    {
         gmailManagerService.getMessageContent(messageID, new Boolean(false));
        org.junit.Assert.assertNotNull(gmailManagerService.getBodyString(messageID));
    }

    @Test
    public void getLabel()
    {
        /*Label label = gmailManagerService.getLabel(gmailManagerService.getLabels().get(3));
        org.junit.Assert.assertNotNull(label);
        org.junit.Assert.assertNotNull(label.getMessagesTotal());
        org.junit.Assert.assertNotNull(label.getName());
        org.junit.Assert.assertNotNull(label.getMessagesUnread());
        org.junit.Assert.assertNotNull(label.getThreadsTotal());
        org.junit.Assert.assertNotNull(label.getThreadsUnread());*/

    }

    @Test
    public void getProfile()
    {
        Profile profile = gmailManagerService.getUserProfile();
        org.junit.Assert.assertNotNull(profile);
        org.junit.Assert.assertNotNull(profile.getMessagesTotal());
        org.junit.Assert.assertNotNull(profile.getThreadsTotal());
        org.junit.Assert.assertNotNull(profile.getEmailAddress());

    }

    @Test
    public void getAttachments()
    {
        try {
            gmailManagerService.getAttachments(messageID, new Boolean(true));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
