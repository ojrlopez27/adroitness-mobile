package edu.cmu.adroitness.client.services.email.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import android.media.Image;

/**
 * Created by sakoju on 12/12/16.
 */

public class GmailFilterQueryInputVO {

    public String getLabelName() {
        return labelName;
    }

    /**
     * use Constants.GMAIL_DEFAULT_LABELS.get() which is a hashmap of default Gmail Labels
     * Label Name default system created labels: read, unread, Social, Promotions, Forums, Updates
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getUserId() {
        return userId;
    }

    /** User ID by default : "me" or getSelectedAccount
     * (the account chosen at the time of launch of the application) **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFolderName() {
        return folderName;
    }

    /** browse Constants.GMAIL_FOLDERS which is a hashmap of Gmail Folders, to populate folder values.
     * Always fill the values from here.
     * Folder Name: INBOX, DRAFTS, SENT, TRASH, ALLMAIL, CHATS **/
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSenderEmailID() {
        return senderEmailID;
    }

    /** sender email id as String: example: "xyz@abcd.com" **/
    public void setSenderEmailID(String senderEmailID) {
        this.senderEmailID = senderEmailID;
    }

    public String getSubject() {
        return subject;
    }

    /** Email Subject as String: example: "Adroitness test" **/
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getAfterDate() {
        return afterDate;
    }

    /*** Date Object. Eg: new Date(System.currentTimeMillis()) or new Date(long someTimeInPast ***/
    public void setAfterDate(Date afterDate) {
        this.afterDate = afterDate;
    }

    public Date getBeforeDate() {
        return beforeDate;
    }

    /** Date Object. Eg: new Date(System.currentTimeMillis()) or new Date(long someTimeInPast **/
    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate;
    }

    public String getMaxResults() {
        return maxResults;
    }

    /** String value of max results of number of emails. Eg: "20" **/
    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public String getFromName() {
        return fromName;
    }

    /** From email id as String: example: "xyz@abcd.com" **/
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    /** You can also search by name in To field of an email. example 1: "Help Desk"
     * example 2:  "John" **/
    public void setToName(String toName) {
        this.toName = toName;
    }

    public Boolean isHasAtttachment() {
        return hasAtttachment;
    }

    /** has Attachment in email: new Boolean(true) or new Boolean(false) **/
    public void setHasAtttachment(Boolean hasAtttachment) {
        this.hasAtttachment = hasAtttachment;
    }

    public String getFileName() {
        return fileName;
    }

    /** filename in attachment to be searched for: example 1: "*.pdf"**/
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** User ID by default : "me" or getSelectedAccount (the account chosen at the time of launch) **/
    String userId;
    /** browse Constants.GMAIL_FOLDERS which is a hashmap of Gmail Folders, to populate folder values.
     * Always fill the values from here.
     * Folder Name: INBOX, DRAFTS, SENT, TRASH, ALLMAIL, CHATS **/
    String folderName;
    /** use Constants.GMAIL_DEFAULT_LABELS.get() which is a hashmap of default Gmail Labels
     * Label Name default system created labels: read, unread, Social, Promotions, Forums, Updates **/
    String labelName;
    /** sender email id as String: example: "xyz@abcd.com" **/
    String senderEmailID;
    /** Email Subject as String: example: "Adroitness test" **/
    String subject;
    //check date format -change to dateTime /Date and add label guidelines
    /** Date Object. Eg: new Date(System.currentTimeMillis()) or new Date(long someTimeInPast **/
    Date afterDate;
    /** Date Object. Eg: new Date(System.currentTimeMillis()) or new Date(long someTimeInPast **/
    Date beforeDate;
    /** String value of max results of number of emails. Eg: "20" **/
    String maxResults;
    /** From email id as String: example: "xyz@abcd.com" **/
    String fromName;
    /** You can also search by name in To field of an email. example 1: "Help Desk"
     * example 2:  "John" **/
    String toName;
    /** has Attachment in email: new Boolean(true) or new Boolean(false) **/
    Boolean hasAtttachment;
    /** filename in attachment to be searched for: example 1: "*.pdf"**/
    String fileName;
    /** any email is starred as yellow, red, blue, green, purple, Orange and and few more special colored marks
     *  any star color marked email is searched for if the value is true. new Boolean(true) or new Boolean(false) **/
    Boolean isStarred;
    /** use Constants.STAR_COLORS.get() which is a hashmap of 12 star colors supported in Gmail.
     * any email is starred as yellow, red, blue, green, purple, Orange and and few more special colored marks **/
    String hasStarColor;

    public String getHasStarColor() {
        return hasStarColor;
    }

    /** use Constants.STAR_COLORS.get() which is a hashmap of 12 star colors supported in Gmail.
     * any email is starred as yellow which is most used star color, and among
     * other colors are: red, blue, green, purple, Orange and and few more special colored marks **/
    public void setHasStarColor(String hasStarColor) {
        this.hasStarColor = hasStarColor;
    }

    public Boolean getStarred() {
        return isStarred;
    }

    /** any email is starred as yellow, red, blue, green, purple, Orange and and few more special colored marks
     *  any star color marked email is searched for if the value is true. new Boolean(true) or new Boolean(false) **/
    public void setStarred(Boolean starred) {
        isStarred = starred;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [session_id = "+userId+", status = "+folderName+"]";
    }

    /**
     * Returns an Image object that can then be painted on the screen.
     * The url argument must specify an absolute {@link URL}. The name
     * argument is a specifier that is relative to the url argument.
     * <p>
     * This method always returns immediately, whether or not the
     * image exists. When this applet attempts to draw the image on
     * the screen, the data will be loaded. The graphics primitives
     * that draw the image will incrementally paint on the screen.
     *
     * @param  url  an absolute URL giving the base location of the image
     * @param  name the location of the image, relative to the url argument
     * @return      the image at the specified URL
     * @see         Image
     */
    public Image getImage(URL url, String name) {
        try {
            return getImage(new URL(url, name), name);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
