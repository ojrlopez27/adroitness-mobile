package edu.cmu.adroitness.client.services.location.control;


import android.app.Activity;

import java.util.ArrayList;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.location.model.LocationEvent;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    protected MessageBroker mMB;
    protected Activity mActivity;

    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    // ****************************** LOCATION ***********************************************

    /**
     * 5. Set default current place
     * @param defaultPlace
     */
    public void setDefaultCurrentPlace(String defaultPlace) {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SET_LOCATION)
                .put(Constants.LOCATION_CURRENT_PLACE, defaultPlace));
    }

    /**
     * 5. Set default current place
     * @param latitude
     * @param longitude
     */
     public void setDefaultCurrentCoordinates(Double latitude, Double longitude) {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_SET_LOCATION)
                .put(Constants.LOCATION_LATITUDE, latitude)
                .put(Constants.LOCATION_LONGITUDE, longitude));
    }


    /**
     * It tells whether location data should be stored and loaded from phone's sd card. If you pass
     * "true" then the location data will be stored into and loaded from the phone, if "false" then
     * the current location will be no longer stored in the phone and it will no try to get these
     * data from the phone.
     * @param readLocationFromFile
     */
    public void setWriteReadLocationFromFile(Boolean readLocationFromFile) {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_WRITE_LOCATION_DATA_FILE)
                .put(Constants.LOCATION_WRITE_READ_DATA_FROM_FILE, readLocationFromFile));
    }

    public LocationEvent getCurrentLocation() {
        return (LocationEvent) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_GET_LOCATION)
                .put(Constants.LOCATION_PLACE_NAME, Constants.LOCATION_CURRENT_PLACE));
    }

    public LocationEvent getLocation(String place) {
        return (LocationEvent) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_GET_LOCATION)
                .put(Constants.LOCATION_PLACE_NAME, place));
    }

    public LocationEvent getLocation(Double longitude, Double latitude){
        return (LocationEvent) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_GET_LOCATION)
                .put(Constants.LOCATION_LONGITUDE, longitude)
                .put(Constants.LOCATION_LATITUDE, latitude));
    }


    /**
     * It resets the current location and remove any file stored in the phone containin location data.
     */
    public void resetCurrentLocation() {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_RESET_CURRENT_LOCATION));
    }

    public void enableHistoryRecord(Boolean enable) {
        mMB.send(ViewHelper.this,
                MBRequest.build(Constants.MSG_ENABLE_HISTORY_LOCATION)
                .put(Constants.LOCATION_HISTORY, enable));
    }

    public ArrayList<LocationEvent> getHistoryLocations() {
        return (ArrayList<LocationEvent>) mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_GET_HISTORY_LOCATIONS));
    }

    public void resetHistoryLocations() {
        mMB.send(ViewHelper.this, MBRequest.build(Constants.MSG_RESET_HISTORY_LOCATIONS));
    }

    public void setMaxNumHistoryLocations(Integer maxNumber) {
        mMB.send(ViewHelper.this, MBRequest.build( Constants.MSG_SET_MAX_NUM_HISTORY_LOCATION)
                .put( Constants.LOCATION_MAX_HISTORY, maxNumber ));
    }


}
