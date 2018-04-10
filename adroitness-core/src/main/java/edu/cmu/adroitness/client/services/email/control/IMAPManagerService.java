package edu.cmu.adroitness.client.services.email.control;

import android.content.Intent;
import android.os.IBinder;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.comm.email.model.ImapManagerEvent;
import edu.cmu.adroitness.client.services.email.model.ImapVO;
import edu.cmu.adroitness.client.services.generic.control.GenericService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

/**
 * Created by TingYao on 4/11/2016.
 */
public class IMAPManagerService extends GenericService {

    private ImapVO imapVO;

    List<Message> messages;
    Message current_msg;

    Properties props;
    Folder inbox;
    String out="";

    public IMAPManagerService()
    {
        super(null);
        if(actions.isEmpty())
        {
            this.actions.add(Constants.ADD_SERVICE_GMAIL_SERVICE);
        }
    }

    public IMAPManagerService(ImapVO imapVO){
        super(null);
        if(actions.isEmpty())
        {
            this.actions.add(Constants.ADD_SERVICE_IMAP_SERVICE);
        }
        this.imapVO = new ImapVO();
        this.imapVO.setHost(imapVO.getHost());
        this.imapVO.setUserName(imapVO.getUserName());
        this.imapVO.setPassword( imapVO.getPassword());
        System.out.println("Imap manager initialized!!");
        //host = "imap.srv.cs.cmu.edu";
        //user = "tingyaoh";
        //passwd = "for2scs3email";
        props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.port","993");
        props.setProperty("mail.imaps.starttls.enable","true");
        messages = new ArrayList<Message>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return super.onBind(intent);
    }

    @Override
    public void doAfterBind()
    {
        super.doAfterBind();
    }

    public void checkInBox(){
        Util.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Session session = Session.getInstance(props, null);
                    Store store = session.getStore("imaps");
                    store.connect(imapVO.getHost().trim(), imapVO.getUserName().trim(), imapVO.getPassword().trim());
                    closeInbox();
                    inbox = store.getFolder("INBOX");

                    inbox.open(Folder.READ_WRITE);
                    FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                    messages = Arrays.asList(inbox.search(ft));
                    System.out.println("num of msg: "+messages.size());
                    ImapManagerEvent event = ImapManagerEvent.build()
                            .setMessageString("Number of messages: "+messages.size());
                    mb.send(IMAPManagerService.this, event);

                    /*
                    Message msg = inbox.getMessage(inbox.getMessageCount());
                    Address[] in = msg.getFrom();
                    for (Address address : in) {
                        System.out.println("FROM:" + address.toString());
                    }
                    Multipart mp = (Multipart) msg.getContent();
                    BodyPart bp = mp.getBodyPart(0);
                    System.out.println("SENT DATE:" + msg.getSentDate());
                    System.out.println("SUBJECT:" + msg.getSubject());
                    System.out.println("CONTENT:" + bp.getContent());*/
                } catch (Exception mex) {
                    mex.printStackTrace();
                }
            }
        });
    }

    public String parseContent(final Message msg){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    out = "um, something i don't understand";
                    String contentType = msg.getContentType().toLowerCase();
                    if(contentType.contains("text/plain")|| contentType.contains("text/html"))
                        out = msg.getContent().toString();
                    else if(contentType.contains("multipart/alternative")){
                        BodyPart bp = ((Multipart) msg.getContent()).getBodyPart(0);
                        out = bp.getContent().toString();
                    }
                    ImapManagerEvent event = ImapManagerEvent.build()
                            .setMessageString(out);
                    mb.send(IMAPManagerService.this, event);

                } catch (Exception mex) {
                    mex.printStackTrace();
                }
            }
        });
        return out;
    }

    public String parseSender(String raw){
        return raw.split("<")[0];
    }

    public void closeInbox(){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if(inbox!=null&&inbox.isOpen())
                        inbox.close(true);
                } catch (Exception mex) {
                    mex.printStackTrace();
                }
            }
        });
    }

    public void markAllRead(){
        Util.execute(new Runnable() {
            @Override
            public void run() {
            try {
                Session session = Session.getInstance(props, null);
                Store store = session.getStore();
                store.connect(imapVO.getHost(), imapVO.getUserName(), imapVO.getPassword());
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);
                FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                messages = Arrays.asList(inbox.search(ft));
                System.out.println("num of msg: " + messages.size());
                Message [] msgArr = new Message[messages.size()];
                messages.toArray(msgArr);
                inbox.setFlags(msgArr, new Flags(Flags.Flag.SEEN), true);
                inbox.close(true);
                ImapManagerEvent event = ImapManagerEvent.build()
                        .setMessageString("All messages read!");
                mb.send(IMAPManagerService.this, event);
                /*
                Message msg = inbox.getMessage(inbox.getMessageCount());
                Address[] in = msg.getFrom();
                for (Address address : in) {
                    System.out.println("FROM:" + address.toString());
                }
                Multipart mp = (Multipart) msg.getContent();
                BodyPart bp = mp.getBodyPart(0);
                System.out.println("SENT DATE:" + msg.getSentDate());
                System.out.println("SUBJECT:" + msg.getSubject());
                System.out.println("CONTENT:" + bp.getContent());*/
            } catch (Exception mex) {
                mex.printStackTrace();
            }
            }
        });
    }


    public int getUnReadNum(){
        return messages.size();
    }

    public Message getMessage(final int order){
        Message msg = null;
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = messages.get(reverseOrder(order));
                ImapManagerEvent event = ImapManagerEvent.build()
                        .setMessageString("Reading Message!");
                mb.send(IMAPManagerService.this, event);
            }
        });
        return msg;
    }

    public int reverseOrder(int order){ return messages.size()-1-order; }

    public void printInfo(){
        System.out.println("IMAPManagerService: ");
        System.out.println("username: "+ imapVO.getUserName());
        System.out.println("pwd: "+ imapVO.getPassword());
        System.out.println("hostname: "+ imapVO.getHost());

    }

    public void searchContent(String query){
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect(imapVO.getHost(), imapVO.getUserName(), imapVO.getPassword());
            closeInbox();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            ContentSearchTerm cst = new ContentSearchTerm(query);
            messages = Arrays.asList(inbox.search(cst));
            ImapManagerEvent event = ImapManagerEvent.build()
                    .setMessageString("Searching messages!");
            mb.send(IMAPManagerService.this, event);

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    /**
     * Search term in email content
     */
    public class ContentSearchTerm extends SearchTerm {
        private String content;

        public ContentSearchTerm(String content) {
            this.content = content;
        }

        @Override
        public boolean match(Message message) {

            String messageContent = parseContent(message);
            if (messageContent.contains(content)) {

                return true;
            }
            /*
            try {

                String contentType = message.getContentType().toLowerCase();
                if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    String messageContent = message.getContent().toString();
                    //if (messageContent.contains(content)&&!message.isSet(Flags.Flag.SEEN)) {
                    if (messageContent.contains(content)) {
                        return true;
                    }
                }
                else if(contentType.contains("multiparty")){

                }
            } catch (MessagingException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }*/
            return false;
        }

    }

    public class FromFieldSearchTerm extends SearchTerm {
        private String fromEmail;

        public FromFieldSearchTerm(String fromEmail) {
            this.fromEmail = fromEmail;
        }

        @Override
        public boolean match(Message message) {
            try {
                Address[] fromAddress = message.getFrom();
                if (fromAddress != null && fromAddress.length > 0) {
                    if (fromAddress[0].toString().contains(fromEmail)&&!message.isSet(Flags.Flag.SEEN)) {
                        return true;
                    }
                }
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }

            return false;
        }

    }
}
