package edu.cmu.adroitness.comm.generic.control.adapters;

import android.app.Activity;
import android.content.Intent;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.calendar.control.CalendarService;
import edu.cmu.adroitness.client.services.email.control.GmailManagerService;

/**
 * Created by oscarr on 3/15/16.
 */
public final class GooglePlayAdapter extends ChannelAdapter {
    private static GooglePlayAdapter instance;

    private GooglePlayAdapter() {
        super();
    }

    public static GooglePlayAdapter getInstance() {
        if (instance == null) {
            instance = new GooglePlayAdapter();
        }
        return instance;
    }

    public void showGooglePlayServicesError(MBRequest mbRequest) {
        try {
            Integer connection = (Integer) mbRequest.get(Constants.GOOGLE_CONNECTION_STATUS);
            Activity activity = (Activity) mbRequest.get(Constants.APP_CONTEXT);
            UtilServiceAPIs.showGooglePlayServicesAvailabilityErrorDialog(connection, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseAccount(MBRequest request) {
        isAccountSelected( (Activity) request.get(Constants.BUNDLE_ACTIVITY_NAME));
    }

    public void isAccountSelected(Activity activity) {
        try {
            if(ResourceLocator.getExistingInstance().getAccount( Constants.GOOGLE_ACCOUNT) == null) {
                UtilServiceAPIs.chooseAccount((activity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDeviceOnline(MBRequest mbRequest) {
        return UtilServiceAPIs.isDeviceOnline(mContext);
    }

    public String getSelectedAccountName(MBRequest mbRequest) {
        return UtilServiceAPIs.credential.getSelectedAccountName();
    }

    public boolean isGooglePlayServicesAvailable(MBRequest mbRequest) {
        return UtilServiceAPIs.isGooglePlayServicesAvailable(mContext);
    }

    public void configAccountName(final MBRequest mbRequest){
        if (UtilServiceAPIs.configAccountName(
                (Activity) mbRequest.get(Constants.BUNDLE_ACTIVITY_NAME),
                (String) mbRequest.get(Constants.ACCOUNT_NAME))) {
            mResourceLocator.setAccount(
                    ((String) mbRequest.get(Constants.ACCOUNT_TYPE)),
                    ((String)  mbRequest.get(Constants.ACCOUNT_NAME)));
            mResourceLocator.lookupService(CalendarService.class).initializeCalendar();
            mResourceLocator.lookupService(GmailManagerService.class).initializeGmail();
        }

        if (UtilServiceAPIs.configAccountName(
                (Activity) mbRequest.get(Constants.BUNDLE_ACTIVITY_NAME),
                (String) mbRequest.get(Constants.ACCOUNT_NAME))) {
            mResourceLocator.setAccount(
                    ((String) mbRequest.get(Constants.ACCOUNT_TYPE)),
                    ((String)  mbRequest.get(Constants.ACCOUNT_NAME)));
            //mResourceLocator.lookupService(GmailManagerService.class).initializeGmail();
        }
     }


    public void launchActivity(MBRequest request) {
        try {
            Class clazz = null;
            if (request.get(Constants.BUNDLE_ACTIVITY_NAME) == null) {
                throw new Exception("The name of the activity doesn't exist. MB cannot create a new activity");
            } else {
                try {
                    clazz = Class.forName((String) request.get(Constants.BUNDLE_ACTIVITY_NAME));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(mContext, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mResourceLocator.addActivityClass(clazz);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
