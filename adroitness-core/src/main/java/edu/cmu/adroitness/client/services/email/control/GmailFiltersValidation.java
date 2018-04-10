package edu.cmu.adroitness.client.services.email.control;

import edu.cmu.adroitness.client.commons.control.Constants;

/**
 * Created by sakoju on 2/24/17.
 */

public class GmailFiltersValidation {
    public static String isDefaultGmailFolder(String gmailFolder)
    {
        if(Constants.GMAIL_FOLDERS.containsValue(gmailFolder.toUpperCase()))
        {
            return gmailFolder;
        }
        return Constants.GMAIL_INVALID_FILTER;
    }

    public static String isDefaultGmailLabel(String gmailLabel)
    {
        if(Constants.GMAIL_DEFAULT_LABELS.containsValue(gmailLabel))
        {
            return gmailLabel;
        }
        return Constants.GMAIL_INVALID_FILTER;
    }
    public static String isGmailStarColors(String starColor)
    {
        if(Constants.STAR_COLORS.containsValue(starColor))
        {
            return starColor;
        }
        return Constants.GMAIL_INVALID_FILTER;
    }

}
