package edu.cmu.adroitness.client.services.email.control;


import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.Profile;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.commons.view.AccountPickerActivity;
import edu.cmu.adroitness.client.services.email.model.GmailFilterQueryInputVO;
import edu.cmu.adroitness.client.services.email.model.GmailMessageVO;
import edu.cmu.adroitness.client.services.generic.control.GenericService;

/**
 * Modified and upgraded (to Gmail API 1.22 rev 49 v1) - by sakoju on 12/23/2016.
 */
public class GmailManagerService extends GenericService {
    private com.google.api.services.gmail.Gmail mGmailService = null;
    private HttpTransport transport ;
    private JsonFactory jsonFactory ;
    public static String defaultUserId = "me";
    public static String defaultMaxResults = "5";
    String[] notificationString ={"all", "messageAdapter", "labels", "none"};
    private String sender = "unknown";
    public static String userID ="";
    private String messageString="";
    private GmailFilterQueryInputVO gmailFilterQueryInputVO;
    //****** Gmail content
    private List<String> labels;
    private List<Message> newMessageList;
    private List<GmailMessageVO> gmailMessageVOList;
    private Integer unreadCounter;
    private String threadID ="";
    private Map<String, String> bodyString;
    private Map<String, Message> messageIdMap;
    private String jsonString;
    private ListMessagesResponse response;
    private Label label ;
    private Profile userProfile;
    private List<GmailMessageVO> sentEmails;
    private List<GmailMessageVO> forwardedEmails;
    private List<GmailMessageVO> repliedEmails;
    private List<GmailMessageVO> allEmails;

    /**
     * update Gmail automatically
     **/
    private Timer timer;
    private static Long mUpdateTime = 10 * 60 * 1000L; // 2 minmUpdateTime




    public GmailManagerService()
    {
        super(null);
        if(actions.isEmpty())
        {
            this.actions.add(Constants.ADD_SERVICE_GMAIL_SERVICE);
        }
        transport = AndroidHttp.newCompatibleTransport();
        jsonFactory = JacksonFactory.getDefaultInstance();
        labels = new ArrayList<>();
        userID=defaultUserId;
        messageIdMap = new HashMap<>();
        allEmails = new ArrayList<>();
    }

    public synchronized void initializeGmail() {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if ( UtilServiceAPIs.credential == null && !UtilServiceAPIs.isAccountSelected) {
                    UtilServiceAPIs.initializeCredentials(mContext, null);
                    mGmailService = new com.google.api.services.gmail.Gmail.Builder(
                            transport, jsonFactory, UtilServiceAPIs.credential)
                            .setApplicationName("Adroitness")
                            .build();
                    if (resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null ) {

                        userID = defaultUserId;
                        try {
                            getDataFromApi();
                        }catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                if(UtilServiceAPIs.credential != null && UtilServiceAPIs.isAccountSelected)
                {
                    mGmailService = new com.google.api.services.gmail.Gmail.Builder(
                            transport, jsonFactory, UtilServiceAPIs.credential)
                            .setApplicationName("Adroitness")
                            .build();
                    if( resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null )
                    {
                        userID = defaultUserId;
                        try {
                            getDataFromApi();
                        }catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        updateLabelListFromGmail();
                        GmailManagerEvent event = GmailManagerEvent.build()
                                .setMessageString("Initialization of Gmail completed")
                                .setUserID(userID);
                        mb.send(GmailManagerService.this, event);
                    }

                }
            }
        });
    }

    /**
     * This default component for latest 10 emails from INBOX folder and unread label or not.
     * (Similar to Calendar)
     */
    private final class GmailTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                if( resourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) != null ) {
                    getDataFromApi();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void checkInitialization(){
        if( mGmailService == null ){
            initializeGmail();
        }
        if( timer == null ) {
            timer = new Timer();
            timer.schedule(new GmailTimerTask(), mUpdateTime, mUpdateTime);
        }
    }

    /***
     * Set Timer similar to Calendar for events
     * @param syncTime
     */
    public void setSyncTime(Long syncTime) {
        mUpdateTime = syncTime;
        if( timer != null ){
            timer.cancel();
            timer = new Timer();
            timer.schedule(new GmailTimerTask(), mUpdateTime, mUpdateTime);
        }
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
        checkInitialization();

    }

    public Integer getUnreadGmailNum(){
        try {
             Label label = mGmailService.users().labels().
                     get(userID, Constants.GMAIL_DEFAULT_LABELS.get(0)).execute();
            unreadCounter = label.getMessagesUnread();
            mb.send(GmailManagerService.this, GmailManagerEvent.build()
                    .setUnreadCounter(unreadCounter)
                    .setNotification(Constants.GMAIL_AFTER_GET_UNREAD));
        }catch(IOException e)
        {
            e.printStackTrace();
        }
         return unreadCounter;

    }

    public Label getLabel(final String labelName)
    {
        Util.execute(new Runnable() {
            @Override
            public void run() {

         label = new Label();
                if(labels.contains(labelName)) {
                    try {
                        label = mGmailService.users().labels().
                                get(userID, labelName).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    label = new Label();
                }
                mb.send(GmailManagerService.this, GmailManagerEvent.build()
                        .setLabel(label)
                .setNotification(Constants.GMAIL_AFTER_GET_LABEL));

            }
        });
        return label;
    }

    public Profile getUserProfile()
    {
        Util.execute(new Runnable() {
            @Override
            public void run() {

                userProfile = new Profile();
                if(userID!= null) {
                    try {
                        userProfile = mGmailService.users().getProfile(userID).execute();
                        mb.send(GmailManagerService.this, GmailManagerEvent.build()
                                .setUserProfile(userProfile)
                                .setNotification(Constants.GMAIL_AFTER_USER_PROFILE));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    userProfile = new Profile();
                    mb.send(GmailManagerService.this, GmailManagerEvent.build()
                            .setUserProfile(userProfile)
                            .setMessageString("No User profile found. Check if user authenticated.")
                            .setNotification(Constants.GMAIL_AFTER_USER_PROFILE));
                }
            }
        });
        return userProfile;
    }

    public void getSender(final String messageID){
        sender = "unknown";
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if(gmailMessageVOList.size()>0)
                {
                    for(GmailMessageVO gmailMessageVO: gmailMessageVOList)
                    {
                        if(gmailMessageVO.getMessageID().equals(messageID))
                        {
                            sender = gmailMessageVO.getFromEmail();
                            GmailManagerEvent event = GmailManagerEvent.build()
                                    .setMessageString("Sender: " + sender);
                            mb.send(GmailManagerService.this, event);
                            break;
                        }
                    }
                }
                if(sender.equals("unknown"))
                {
                    GmailMessageVO gmailVO =
                            getJsonEmailMessageDetails(messageID, new Boolean(false));
                    sender = gmailVO.getFromEmail();
                    GmailManagerEvent event = GmailManagerEvent.build()
                            .setMessageString("Sender: " + sender);
                    mb.send(GmailManagerService.this, event);
                }

            }
        });
    }

    public void updateLabelListFromGmail() {
        Util.execute(new Runnable() {
            @Override
            public void run() {

                String user = defaultUserId;
                    labels = new ArrayList<>();
                try {

                    ListLabelsResponse listResponse = new ListLabelsResponse();
                    try {
                        listResponse = mGmailService.users().labels().list(userID).execute();
                    }catch (UserRecoverableAuthIOException userRecoverableException) {
                        sendUserPermissionRequest(userRecoverableException);
                    }
                    if(listResponse.size()>0) {
                        if(!checkIfNewLabels(listResponse)) {
                            boolean sendNotfication= true;
                            for (Label label : listResponse.getLabels()) {
                                labels.add(label.getName());
                                Log.d("Labels:", label.getName());
                                sendNotfication=true;
                            }
                            if(sendNotfication)
                            {
                                sendGmailData(notificationString[2]);
                            }
                        }
                    }

                } catch (Exception e){
                    Log.d("ExceptionInUpdate",e.toString());

                }
            }
        });
    }

    /***
     * Get data from Gmail Api with complete google OAuth authentication checks
     */
    public void getDataFromGmail() {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                    if (UtilServiceAPIs.credential != null) {
                        if (UtilServiceAPIs.credential.getSelectedAccountName() == null
                                && !UtilServiceAPIs.chooseAccountInProcess) {
                            UtilServiceAPIs.chooseAccount(null);

                            getDataFromGmail();
                        } else {
                            if (UtilServiceAPIs.isAccountSelected) {
                                if (UtilServiceAPIs.isDeviceOnline( null )) {
                                    try {
                                        getDataFromApi();
                                        updateLabelListFromGmail();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }

                                } else {
                                    mb.send(GmailManagerService.this,
                                            GmailManagerEvent.build()
                                                    .setMessageString("No network connection available."));
                                }
                            }
                            else
                            {
                                    Util.sleep(1000);
                                    getDataFromGmail();
                            }

                        }
                    } else {
                        initializeGmail();
                    }
            }
        });
    }

    private void initializeParameters()
    {


        GmailFilterQueryInputVO gmailFilterQueryInputVO = new GmailFilterQueryInputVO();
        gmailFilterQueryInputVO.setUserId(defaultUserId);
        gmailFilterQueryInputVO.setFolderName(
                GmailFiltersValidation.isDefaultGmailFolder(Constants.GMAIL_FOLDERS.get(0)));
        gmailFilterQueryInputVO.setLabelName(
                GmailFiltersValidation.isDefaultGmailLabel(Constants.GMAIL_DEFAULT_LABELS.get(0)));
        gmailFilterQueryInputVO.setMaxResults(defaultMaxResults);
        gmailFilterQueryInputVO.setHasAtttachment(false);
        //gmailFilterQueryInputVO.setFileName("*.pdf");
        gmailFilterQueryInputVO.setBeforeDate(new Date(System.currentTimeMillis()+100000000));
        setGmailFilterQueryInputVO(gmailFilterQueryInputVO);

    }

    /**
     * Fetch a list of Gmail labels attached to the specified account.
     * @return List of Strings labels.
     * @throws IOException
     */
    public void getDataFromApi() throws IOException {
        Util.execute(new Runnable() {
            @Override
            public void run()
            {
                // Get the labels in the user's account.
                ListLabelsResponse listResponse = new ListLabelsResponse();
                initializeParameters();
                try {
                    if(labels!=null)
                    {
                        if(labels.size()==0)
                        {
                            try {
                                 listResponse = mGmailService.users().labels().list(userID).execute();
                            }
                            catch (UserRecoverableAuthIOException userRecoverableException)
                            {
                                sendUserPermissionRequest(userRecoverableException);
                            }

                            if(listResponse.size()>0 ) {
                                if(!checkIfNewLabels(listResponse)) {
                                    for (Label label : listResponse.getLabels()) {
                                        labels.add(label.getName());
                                    }
                                }
                                sendGmailData(notificationString[2]);
                            }

                            try {
                                filterMessagesByQuery(gmailFilterQueryInputVO,
                                        new Boolean(false));
                                Log.d("filterMessagesByQuery", "Completed");
                            }catch (UserRecoverableAuthIOException userRecoverableException) {
                                sendUserPermissionRequest(userRecoverableException);
                            }
                        }
                    }
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }


    private Boolean checkIfNewLabels(ListLabelsResponse listLabelsResponse)
    {
        Boolean flag = new Boolean(false);
        if(labels!=null)
        {
            if(labels.size()>0)
            {
                for (Label label : listLabelsResponse.getLabels())
                {
                    if(!labels.contains(label.getName()))
                    {
                        labels.add(label.getName());
                        if(!flag) {
                            flag = new Boolean(true);
                        }
                    }
                }
            }

        }
        return flag;
    }

    /**
     * Send Gmail Manager Event Data
     */
    private void sendGmailData(String typeOfDataChange)
    {
        switch (typeOfDataChange)
        {
            case "labels":  if(labels.size()>0) {
                                mb.send(GmailManagerService.this,
                                    GmailManagerEvent.build()
                                    .setNotification(Constants.GMAIL_AFTER_UPDATE_LABEL_LIST)
                                    .setLabels(labels));
                                }
                                break;
            case "messageAdapter": if(gmailMessageVOList.size()>0)
                                {
                                    mb.send(GmailManagerService.this, GmailManagerEvent.build()
                                            .setNotification(Constants.GMAIL_AFTER_FILTER_QUERY)
                                    .setEmails(gmailMessageVOList));
                                }
                                break;
            case "all":         if(allEmails.size()>0)
                                {
                                    mb.send(GmailManagerService.this, GmailManagerEvent.build()
                                            .setNotification(Constants.GMAIL_GET_ALL_EMAILS)
                                    .setAllEmails(allEmails));
                                }
                                break;
            case "none":
                                mb.send(GmailManagerService.this, GmailManagerEvent.build()
                                            .setMessageString("Filtered by query: "+
                                                    " \nTotal Count: "+" 0 "
                                                 +"There were no emails returned by Gmail API!"));
                                break;
             default: break;
        }
    }

    private void sendUserPermissionRequest(UserRecoverableAuthIOException exception)
    {
        mb.send(GmailManagerService.this,
                GmailManagerEvent.build()
                        .setNotification(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)
                        .setParams(exception.getIntent()));
    }
    /**
     * List all Messages of the user's mailbox with labelIds applied.
     * GmailFilterQueryInputVO User's query details (user = me, non-null MAx Results).
     * @throws IOException
     */
    public void filterMessagesByQuery(final GmailFilterQueryInputVO gmailFilterQueryInputVO, final Boolean isSendEvent)
            throws IOException {
    Util.execute(new Runnable() {
        @Override
        public void run() {


            List<String> labelIds = new ArrayList<String>();
            response= new ListMessagesResponse();
            newMessageList = new ArrayList<>();
            //get the query format required for Gmail API and send GmailManagerEvent
            String query=getQuery( gmailFilterQueryInputVO);

            labelIds.add(gmailFilterQueryInputVO.getFolderName());

            try {
                 response = mGmailService.users().messages()
                         .list(gmailFilterQueryInputVO.getUserId())
                         .setLabelIds(labelIds).setMaxResults(
                                Long.parseLong(gmailFilterQueryInputVO.getMaxResults())).setQ(query).execute();
            }
            catch (UserRecoverableAuthIOException userRecoverableException) {
                sendUserPermissionRequest(userRecoverableException);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            int counter=1;
            int maxResults = Integer.parseInt(gmailFilterQueryInputVO.getMaxResults());

            while (response.getMessages() != null && counter<=1) {
                newMessageList.addAll(response.getMessages());
                String pageToken = response.getNextPageToken();
                if (pageToken != null && counter<=1 && messageIdMap.size()<=10) {
                    try {
                        response = mGmailService.users().messages()
                                .list(gmailFilterQueryInputVO.getUserId()).setLabelIds(labelIds)
                                .setMaxResults(Long.parseLong(gmailFilterQueryInputVO.getMaxResults()))
                                .setQ(query).setPageToken(pageToken).execute();
                    }catch (UserRecoverableAuthIOException userRecoverableException) {
                        sendUserPermissionRequest(userRecoverableException);
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    counter++;
                } else {
                    break;
                }
            }
            if(checkifNewEmails())
            {
                if(messageIdMap.size()>0) {
                    updateEmailDetailsList();
                    Log.d("UpdateEmailDetailsList", "Completed");
                }
                else
                {
                    sendGmailData(notificationString[3]);
                }
            }
            if(isSendEvent)
            {
                if(gmailMessageVOList.size()>0) {
                    sendGmailData(notificationString[1]);
                }
            }
            userID = gmailFilterQueryInputVO.getUserId();
        }
    });

    }

    private Boolean checkifNewEmails() {

       return Util.executeSync(new Callable<Boolean>() {
            Boolean flag = new Boolean(false);

            @Override
            public Boolean call() throws Exception {
                if (messageIdMap != null && newMessageList != null) {
                    if (newMessageList.size() > 0) {
                        if (messageIdMap.size() > 0) {
                           ListIterator<Message> listIterator = newMessageList.listIterator();
                            while (listIterator.hasNext()) {
                                Message message = listIterator.next();
                                if (!messageIdMap.containsKey(message.getId())) {
                                    messageIdMap.put(message.getId(), message);
                                    flag = new Boolean(true);
                                }
                            }
                        } else {
                            ListIterator<Message> listIterator = newMessageList.listIterator();
                            while (listIterator.hasNext()) {
                                Message message = listIterator.next();
                                messageIdMap.put(message.getId(), message);
                                flag = new Boolean(true);
                            }
                        }

                    }
                }
                return flag;
            }
        });
    }

    private synchronized void updateEmailDetailsList()
    {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                String typeOfNotification ="";
                Log.d("messageListSize", String.valueOf(messageIdMap.size()));
                bodyString = new HashMap<>();
                int i=0;
                gmailMessageVOList = new ArrayList<>();
                /*for (Message message : newMessageList.subList(0,newMessageList.size())) {

                    threadID = message.getThreadId();
                    GmailMessageVO gmailMessageVO = getJsonEmailMessageDetails(message.getId(), new Boolean(false));
                    gmailMessageVOList.add(gmailMessageVO);
                    if (!messageString.isEmpty()) {
                        typeOfNotification += !typeOfNotification.contains("messageAdapter") ?
                                "messageAdapter" : "";
                    }
                    Log.d("jsonString", jsonString);
                    i++;
                }*/
                if(allEmails.size()>0) {
                    for (GmailMessageVO gmailMessageVO : allEmails) {
                        if (!messageIdMap.containsKey(gmailMessageVO.getMessageID())) {
                            threadID = messageIdMap.get(gmailMessageVO.getMessageID()).getThreadId();
                            GmailMessageVO newGmailMessageVO =
                                    getJsonEmailMessageDetails(gmailMessageVO.getMessageID(), new Boolean(false));
                            allEmails.add(newGmailMessageVO);
                            gmailMessageVOList.add(newGmailMessageVO);
                            typeOfNotification += !typeOfNotification.contains(notificationString[1]) ?
                                    notificationString[1] : "";

                            Log.d("jsonString", jsonString);
                            i++;
                        }
                    }
                    if(typeOfNotification.contains(notificationString[1]))
                    {
                        sendGmailData(notificationString[1]);
                    }
                    if(gmailMessageVOList.size()==0)
                    {
                        gmailMessageVOList.addAll(allEmails);
                    }
                }
                else
                {
                    allEmails = new ArrayList<>();
                    for (Message message : messageIdMap.values()) {

                        threadID = message.getThreadId();
                        GmailMessageVO gmailMessageVO =
                                getJsonEmailMessageDetails(message.getId(), new Boolean(false));
                        gmailMessageVOList.add(gmailMessageVO);
                        allEmails.add(gmailMessageVO);
                        Log.d("jsonString", jsonString);
                        i++;
                    }
                    if(allEmails.size()>0)
                    {
                        sendGmailData(notificationString[0]);
                    }
                    if(gmailMessageVOList.size()>0)
                    {
                        sendGmailData(notificationString[1]);
                    }
                }
            }
        });
    }

    private String getQuery( GmailFilterQueryInputVO gmailFilterQueryInputVO)
    {

        String query="";
        if(gmailFilterQueryInputVO.getFolderName()!=null )
        {
            String folderName= GmailFiltersValidation.isDefaultGmailFolder(gmailFilterQueryInputVO.getFolderName());
            if(folderName!=Constants.GMAIL_INVALID_FILTER) {
                query += "in:" + folderName.toLowerCase();
            }
        }
        if(gmailFilterQueryInputVO.getLabelName()!=null)
        {
            String labelName= GmailFiltersValidation.isDefaultGmailFolder(gmailFilterQueryInputVO.getLabelName());
            if(labelName!=Constants.GMAIL_INVALID_FILTER) {
                query += " is:" + labelName.toLowerCase();
            }
            else
            {
                if(labels.contains(gmailFilterQueryInputVO.getLabelName()))
                {
                    query += " is:" + gmailFilterQueryInputVO.getLabelName().toLowerCase();
                }
            }
        }
        if(gmailFilterQueryInputVO.getSenderEmailID()!=null )
        {
            query += " from:"+gmailFilterQueryInputVO.getSenderEmailID();
        }
        if(gmailFilterQueryInputVO.getSubject()!=null )
        {
            query += " subject:"+gmailFilterQueryInputVO.getSubject();
        }
        if(gmailFilterQueryInputVO.getAfterDate()!=null )
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            query += " after:"+formatter.format(gmailFilterQueryInputVO.getAfterDate());
        }
        if(gmailFilterQueryInputVO.getBeforeDate()!=null )
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            query += " before:"+formatter.format(gmailFilterQueryInputVO.getBeforeDate());

        }
        if(gmailFilterQueryInputVO.getFromName()!=null)
        {
            query += " from:"+gmailFilterQueryInputVO.getFromName();
        }
        if(gmailFilterQueryInputVO.getToName()!=null)
        {
            query += " to:"+gmailFilterQueryInputVO.getToName();
        }
        if(gmailFilterQueryInputVO.isHasAtttachment())
        {
            query+=" has:attachment";
        }
        if(gmailFilterQueryInputVO.getFileName()!=null)
        {
            query+= " filename:"+gmailFilterQueryInputVO.getFileName();
        }
        if(gmailFilterQueryInputVO.getStarred()!=null)
        {
            if(gmailFilterQueryInputVO.getStarred()) {
                query += " is:" + "starred";
            }
        }
        if(gmailFilterQueryInputVO.getHasStarColor()!=null)
        {
            String colorValue= GmailFiltersValidation.isGmailStarColors(gmailFilterQueryInputVO.getHasStarColor());
            if(colorValue!=Constants.GMAIL_INVALID_FILTER) {
                query += " has:" + colorValue.toLowerCase();
            }
        }
        return query;
    }

    /**
     * Get a Message and use it to create a MimeMessage.
     * The special value "me"
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return MimeMessage MimeMessage populated from retrieved Message.
     * @throws IOException
     * @throws MessagingException
     */
    public MimeMessage getMimeMessage( String messageId)
            throws IOException, MessagingException {
        Message message = new Message();
        try {
             message = mGmailService.users().messages().get(userID, messageId).setFormat("raw").execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        Base64 base64Url = new Base64(true);
        byte[] emailBytes = base64Url.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        GmailManagerEvent event = GmailManagerEvent.build()
                .setEmail(email);
        mb.send(GmailManagerService.this, event);
        return email;
    }

    /**
     * Get Message with given ID.
     * User's email address. The special value "me"
     * can be used to indicate the authenticated user
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    private Message getMessage( String messageId)
            throws IOException {
        Message message = new Message();
        try {
             message = mGmailService.users().messages().get(userID, messageId).execute();
        }
        catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        Log.d("emailSnippet","Message snippet: " + message.getSnippet());

        return message;
    }

    /***
     * get first unread email message
     * @return get message ID of first unread email
     */
    public String getFirstUnreadEmailMessageID()
    {
        return messageIdMap.entrySet().iterator().next().getKey();
    }

    /***
     * get JSON Format of email Message (requires extracting actual
     * content that could be Multipart or plain text or html
     * @param messageID
     */
    public GmailMessageVO getJsonEmailMessageDetails( final String messageID , final Boolean isListViewItemClick)
    {
        return Util.executeSync(new Callable<GmailMessageVO>() {

            @Override
            public GmailMessageVO call() throws Exception {
                GmailMessageVO gmailMessageVO = new GmailMessageVO();
                //convert mime message to json here: format:
                jsonString ="";
                String userEmail = UtilServiceAPIs.credential.getSelectedAccountName();
                String subject="";
                MimeMessage mimeMessage;

                try {
                    mimeMessage = getMimeMessage(messageID);
                    jsonString = "{ messageID: "+messageID;
                    jsonString += ", from:"+mimeMessage.getFrom()[0].toString();
                    jsonString += ", sentDate:"+mimeMessage.getSentDate().toString();
                    jsonString += ", receivedDate:"+mimeMessage.getReceivedDate();
                    if(mimeMessage.getSubject()!=null)
                    {
                        subject = mimeMessage.getSubject();
                    }
                    jsonString += ", subject:"+subject;

                    messageString= "MessageID: "+ messageID +" , from:"+mimeMessage.getFrom()[0].toString()
                            + ", subject:"+subject;

                    String ccEmailList="";
                    if(mimeMessage.getAllRecipients().length>1)
                    {
                        for(Address address: mimeMessage.getAllRecipients())
                        {
                            if(!address.toString().equals(userEmail))
                            {
                                ccEmailList+=ccEmailList.isEmpty()?address.toString():(","+address.toString());
                            }
                        }
                    }
                    gmailMessageVO.setCcEmail(ccEmailList.trim());
                    gmailMessageVO.setMessageID(messageID);
                    gmailMessageVO.setThreadID(threadID);
                    gmailMessageVO.setEmailDate(mimeMessage.getSentDate());
                    gmailMessageVO.setFromEmail(mimeMessage.getFrom()[0].toString());
                    gmailMessageVO.setSubject(subject);
                    if(mimeMessage.getFileName()!=null) {
                        if (!mimeMessage.getFileName().isEmpty()) {
                            gmailMessageVO.setHasAttachment(true);
                        } else {
                            gmailMessageVO.setHasAttachment(false);
                        }
                    }


                    if(mimeMessage.isMimeType("\"text/plain\"")) {
                        bodyString.put(messageID, "{ " + mimeMessage.getContent().toString() + " }");
                        gmailMessageVO.setMessageContent(mimeMessage.getContent().toString());
                    }
                    else if(mimeMessage.isMimeType("multipart/*"))
                    {
                        MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
                        String mimeMultipartContent= getTextFromMimeMultipart(mimeMultipart);
                        bodyString.put(messageID, "{ " +  mimeMultipartContent+ " }");
                        gmailMessageVO.setMessageContent(mimeMultipartContent);
                    }
                    if(bodyString.get(messageID)==null)
                    {
                        if(mimeMessage.getContentType().contains("text/plain"))
                        {
                            bodyString.put(messageID, mimeMessage.getContent().toString());
                        }
                    }
                    gmailMessageVO.setJsonString(jsonString);
                    if(isListViewItemClick) {
                        GmailManagerEvent event = GmailManagerEvent.build()
                                .setNotification(Constants.GMAIL_GET_MESSAGE_VO)
                                .setGmailMessageVO(gmailMessageVO);
                        mb.send(GmailManagerService.this, event);
                    }
                }catch(MessagingException e)
                {
                    e.printStackTrace();
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
                Log.d("getJson", "Completed");
                return gmailMessageVO;
            }
        });
    }

    /***
     * Get email body in text format from content type Multipart from Multipart
     * @param mimeMultipart
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private synchronized String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException
    {
        String result="";
        int count = mimeMultipart.getCount();
        if(count==0)
        {
            result += "MimeMultipart message with no body is not allowed.";
        }
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if(multipartAlt)
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count-1));
        for(int i=0; i<count ; i++)
        {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        //Log.d("MimeMultiPart2Text",result);
        return result;

    }

    /***
     * Check email body content type (plain text, html or Multipart) and
     * return email content
     * @param bodyPart
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private synchronized String getTextFromBodyPart( BodyPart bodyPart) throws IOException, MessagingException
    {
        String result ="";
        if(bodyPart.isMimeType("text/plain"))
        {
            result = (String) bodyPart.getContent();
        }
        else if(bodyPart.isMimeType("text/html"))
        {
            String html = (String) bodyPart.getContent();
            result = Jsoup.parse(html).text();
        }
        else if(bodyPart.getContent() instanceof MimeMultipart)
        {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        //Log.d("TextInGmailBody",result);
        return result;
    }

    /***
     * Retrieve a Email body from MimeMessage
     * @param messageID
     * @return
     */
    public void getMessageContent( String messageID, Boolean isActivityRequest)
    {
        String body ="";
        if(bodyString!=null)
        {
            body = bodyString.get(messageID)==null?"":bodyString.get(messageID);
        }
        if(body.equals(""))
        {
            try {
                MimeMessage mimeMessage= getMimeMessage(messageID);
                if (mimeMessage.isMimeType("\"text/plain\"")) {
                    bodyString.put(messageID, "{ " + mimeMessage.getContent().toString() + " }");
                } else if (mimeMessage.isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
                    bodyString.put(messageID, "{ " + getTextFromMimeMultipart(mimeMultipart) + " }");
                }
            }catch(IOException ex)
            {
                ex.printStackTrace();
            }
            catch(MessagingException exc)
            {
                exc.printStackTrace();
            }
        }
        if(isActivityRequest) {
            GmailManagerEvent event = GmailManagerEvent.build()
                    .setNotification(Constants.GMAIL_GET_MESSAGE_CONTENT)
                    .setMessageBody(body)
                    .setMessageString(messageID);
            mb.send(GmailManagerService.this, event);
        }
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    private MimeMessage createEmail(String to,
                                   String from,
                                   String subject,
                                   String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }


    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    private MimeMessage createEmailWithAttachment(String to,
                                                        String from,
                                                        String subject,
                                                        String bodyText,
                                                        File file)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * can be used to indicate the authenticated user.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public void sendMessage(GmailMessageVO inputGmailMessageVO)
            throws MessagingException, IOException {
        String to = inputGmailMessageVO.getToEmail();
        String subject = inputGmailMessageVO.getSubject();
        String bodyText = inputGmailMessageVO.getMessageContent();
        String from;
        if(inputGmailMessageVO.getFromEmail()!=null)
        {
            from = inputGmailMessageVO.getFromEmail();
        }
        else {
            from = UtilServiceAPIs.credential.getSelectedAccountName();
        }
        MimeMessage emailContent = createEmail(to, from, subject, bodyText);
        Message message = createMessageWithEmail(emailContent);
        try {
            message = mGmailService.users().messages().send(from, message).execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setThreadID(message.getThreadId());
        gmailMessageVO.setMessageID(message.getId());
        gmailMessageVO.setSubject(subject);
        gmailMessageVO.setToEmail(to);
        gmailMessageVO.setMessageContent(bodyText);
        gmailMessageVO.setHasAttachment(false);
        sentEmails = new ArrayList<>();
        sentEmails.add(gmailMessageVO);

        Log.d("sendEmail","Message id: " + message.getId());
        Log.d("sendEmail",message.toPrettyString());
        GmailManagerEvent event = GmailManagerEvent.build()
                .setMessageString("Email message Sent: Message ID : " + message.getId())
                .setNotification(Constants.GMAIL_AFTER_SEND_EMAIL)
                .setSentEmails(sentEmails);
        mb.send(GmailManagerService.this, event);
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     * can be used to indicate the authenticated user.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public void sendMessageWithAttachment(GmailMessageVO inputGmailMessageVO)
            throws MessagingException, IOException {
        String to = inputGmailMessageVO.getToEmail();
        String subject = inputGmailMessageVO.getSubject();
        String bodyText = inputGmailMessageVO.getMessageContent();
        File file = inputGmailMessageVO.getAttachedFile();
        String from;
        if(inputGmailMessageVO.getFromEmail()!=null)
        {
            from = inputGmailMessageVO.getFromEmail();
        }
        else {
            from = UtilServiceAPIs.credential.getSelectedAccountName();
        }
        MimeMessage emailContent = createEmailWithAttachment(to, from, subject, bodyText, file);
        Message message = createMessageWithEmail(emailContent);
        try {
            message = mGmailService.users().messages().send(from, message).execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setThreadID(message.getThreadId());
        gmailMessageVO.setMessageID(message.getId());
        gmailMessageVO.setSubject(subject);
        gmailMessageVO.setToEmail(to);
        gmailMessageVO.setMessageContent(bodyText);
        gmailMessageVO.setHasAttachment(true);
        sentEmails = new ArrayList<>();
        sentEmails.add(gmailMessageVO);

        Log.d("sendEmailAtt","Message id: " + message.getId());
        Log.d("sendEmailAtt",message.toPrettyString());
        GmailManagerEvent event = GmailManagerEvent.build()
                .setMessageString("Email message Sent: Message ID : " + message.getId())
                .setSentEmails(sentEmails);
        mb.send(GmailManagerService.this, event);

    }

    /***
     *
     * @param inputGmailMessageVO
     * @throws IOException
     * @throws MessagingException
     */
    public void replyToEmail(GmailMessageVO inputGmailMessageVO) throws IOException, MessagingException
    {
        String addRecipientEmails = inputGmailMessageVO.getToEmail()==null?"":inputGmailMessageVO.getToEmail();
        String body = inputGmailMessageVO.getMessageContent();
        String messageId = inputGmailMessageVO.getMessageID();
        Boolean replyAllFlag = inputGmailMessageVO.getReplyAllFlag();
        Message message = new Message();

        try{
        message = mGmailService.users().messages().get(defaultUserId, messageId).setFormat("raw").execute();
    }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
    }
        Base64 base64Url = new Base64(true);
        byte[] emailBytes = base64Url.decodeBase64(message.getRaw());
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        String subject = "Re: "+ email.getSubject();

        MimeMessage replyMessage = (MimeMessage) email.reply(replyAllFlag);
        String replyTo="";
        if(replyAllFlag)
        {
            replyTo  =  InternetAddress.toString(email.getAllRecipients());
            Log.d("replyTo", replyTo);

            replyMessage.setReplyTo(email.getFrom());
        }
        else
        {
            replyMessage.setReplyTo(email.getFrom());
        }

        if(addRecipientEmails.contains(",")) {
            replyMessage.addRecipients(javax.mail.Message.RecipientType.CC, addRecipientEmails);
        }else if(!addRecipientEmails.equals(""))
        {
            replyMessage.addRecipient(javax.mail.Message.RecipientType.CC,new InternetAddress( addRecipientEmails));
        }

        replyMessage.setText(body);
        replyMessage.saveChanges();
        Message replyGmailMessage = createMessageWithEmail(replyMessage);
        replyGmailMessage.setThreadId(message.getThreadId());
        Message resultEmail = new Message();
        try {
             resultEmail = mGmailService.users().messages().send(defaultUserId, replyGmailMessage).execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setThreadID(resultEmail.getThreadId());
        gmailMessageVO.setMessageID(resultEmail.getId());
        gmailMessageVO.setSubject(subject);
        gmailMessageVO.setToEmail(addRecipientEmails);
        gmailMessageVO.setMessageContent(body);
        gmailMessageVO.setHasAttachment(false);
        repliedEmails = new ArrayList<>();
        repliedEmails.add(gmailMessageVO);

        mb.send(GmailManagerService.this, GmailManagerEvent.build()
                .setNotification(Constants.GMAIL_AFTER_REPLY_EMAIL)
                .setRepliedEmails(repliedEmails));

        Log.d("replyEmail", "Message id: " + resultEmail.getId());
        Log.d("replyEmail",resultEmail.toPrettyString());


    }

    /***
     *Forward email
     * @param inputGmailMessageVO
     * @throws IOException
     * @throws MessagingException
     */
    public void forwardEmail(GmailMessageVO inputGmailMessageVO) throws IOException, MessagingException
    {
        String toList = inputGmailMessageVO.getToEmail();
        String body = inputGmailMessageVO.getMessageContent();
        String messageId = inputGmailMessageVO.getMessageID();
        Message message = new Message();
        try {
            message = mGmailService.users().messages().get(defaultUserId, messageId).setFormat("raw").execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }
        Base64 base64Url = new Base64(true);
        byte[] emailBytes = base64Url.decodeBase64(message.getRaw());
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mainMessage = getMimeMessage(messageId);
        MimeMessage forward = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        getMessageContent(messageId, false);
        // Fill in header
        forward.setRecipients(MimeMessage.RecipientType.TO,
                InternetAddress.parse(toList));
        forward.setSubject("Fwd: " + forward.getSubject());
        // Create the message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        // set content
        messageBodyPart.setContent(mainMessage, "message/rfc822");
        // Add part to multi part
        multipart.addBodyPart(messageBodyPart);
        // Associate multi-part with message
        forward.setContent(multipart);
        forward.saveChanges();
        Message forwardGmailMessage = createMessageWithEmail(forward);
        forwardGmailMessage.setThreadId(message.getThreadId());
        Message resultEmail = new Message();
        try {
            resultEmail = mGmailService.users().messages().send(defaultUserId, forwardGmailMessage).execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }

        GmailMessageVO gmailMessageVO = new GmailMessageVO();
        gmailMessageVO.setThreadID(resultEmail.getThreadId());
        gmailMessageVO.setMessageID(resultEmail.getId());
        gmailMessageVO.setSubject(forward.getSubject());
        gmailMessageVO.setToEmail(toList);
        gmailMessageVO.setMessageContent(body);
         forwardedEmails = new ArrayList<>();
        forwardedEmails.add(gmailMessageVO);

        mb.send(GmailManagerService.this, GmailManagerEvent.build()
                .setNotification(Constants.GMAIL_AFTER_FORWARD_EMAIL)
                .setForwardedEmail(forwardedEmails));
        Log.d("ForwardEmail","Message id: " + resultEmail.getId());
        Log.d("ForwardEmail",resultEmail.toPrettyString());
    }


    /**
     * Get the attachments in a given email.
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message containing attachment..
     * @throws IOException
     */
    public void getAttachments(String messageId, Boolean saveToPhone)
            throws IOException {
        String path ="";
        Boolean isFileSaved= new Boolean(false);
        File externalDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GmailAttachments/");
        if(externalDir.exists())
        {
            externalDir.mkdir();
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/GmailAttachments"+messageId+"/";

        Message message = new Message();
        try {
            message = mGmailService.users().messages().get(userID, messageId).execute();
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            sendUserPermissionRequest(userRecoverableException);
        }
        if(message.getPayload()!=null)
        {
            if(message.getPayload().getParts()!=null)
            {
                List<MessagePart> parts = message.getPayload().getParts();
                for (MessagePart part : parts) {
                    if (part.getFilename() != null && part.getFilename().length() > 0) {
                        String filename = part.getFilename();
                        String attId = part.getBody().getAttachmentId();
                        MessagePartBody attachPart = new MessagePartBody();
                        try {
                            attachPart = mGmailService.users().messages().attachments().
                                    get(userID, messageId, attId).execute();
                        }catch (UserRecoverableAuthIOException userRecoverableException) {
                            sendUserPermissionRequest(userRecoverableException);
                        }

                        Base64 base64Url = new Base64(true);
                        byte[] fileByteArray = base64Url.decodeBase64(attachPart.getData());
                        FileOutputStream fileOutFile =
                                new FileOutputStream(path + filename);
                        fileOutFile.write(fileByteArray);
                        fileOutFile.close();
                        isFileSaved = new Boolean(true);
                        if(!saveToPhone)
                        {
                            //save to external S3 storage
                            //File outputFile = new File(path+filename);
                            //Util.uploadtoAmazonS3(Context context, String poolId, String bucketName, new String(""), File file)
                            //outputFile.delete();
                        }
                    }

                }
                if(isFileSaved)
                {
                    mb.send(GmailManagerService.this, GmailManagerEvent.build()
                            .setNotification(Constants.GMAIL_GET_ATTACHMENTS)
                    .setFilesDirectory(path));
                }
                else
                {
                    messageString = "no Attachments found for "+messageId;
                    mb.send(GmailManagerService.this, GmailManagerEvent.build()
                            .setNotification(Constants.GMAIL_GET_ATTACHMENTS)
                    .setMessageString(messageString));
                }

            }
        }

    }

    public void onEvent(MiddlewareNotificationEvent event){
        if( event.getSourceName().equals( AccountPickerActivity.class.getName() ) ){
            initializeGmail();
        }
    }

    /*** Getters and Setters
     * UnComment the methods below if running unit test cases in GmailTest.java ***/

    /*
    public List<GmailMessageVO> getSentEmails() {
        return sentEmails;
    }
    public List<GmailMessageVO> getRepliedEmails() {
        return repliedEmails;
    }
    public List<GmailMessageVO> getForwardedEmails() {
        return forwardedEmails;
    }

    public List<String> getLabels() {
        return labels;
    }
    public GmailFilterQueryInputVO getGmailFilterQueryInputVO() {
        return gmailFilterQueryInputVO;
    }*/
    public List<GmailMessageVO> getGmailMessageVOList() {
        return gmailMessageVOList;
    }

    public void setGmailFilterQueryInputVO(GmailFilterQueryInputVO gmailFilterQueryInputVO) {
        this.gmailFilterQueryInputVO = gmailFilterQueryInputVO;
    }

    public String getBodyString(String mId) {
        return bodyString.get(mId);
    }
    public String getSenderString(String msgID) {
        getSender(msgID);
        return sender;
    }
}