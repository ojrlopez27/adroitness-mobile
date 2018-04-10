package edu.cmu.adroitness.comm.generic.control.adapters;

import java.util.ArrayList;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.location.model.LocationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.location.control.LocationService;
import edu.cmu.adroitness.client.services.location.model.LocationVO;

/**
 * Created by oscarr on 3/15/16.
 */
public final class LocationAdapter extends ChannelAdapter {
    private static LocationAdapter instance;

    private LocationAdapter() {
        super();
    }

    public static LocationAdapter getInstance() {
        if (instance == null) {
            instance = new LocationAdapter();
        }
        return instance;
    }

    public void resetHistoryLocations( MBRequest request){
        mResourceLocator.addRequest( LocationService.class, "resetHistoryLocations");

     }

    public void setMaxNumHistoryLocations( MBRequest request){
        mResourceLocator.addRequest( LocationService.class, "setMaxNumHistoryLocations",
                ((Integer) request.get(Constants.LOCATION_MAX_HISTORY)));
     }

    public void writeLocationDataToFile( MBRequest request){
        mResourceLocator.addRequest( LocationService.class, "writeLocationDataToFile",
                ((Boolean) request.get(Constants.LOCATION_WRITE_READ_DATA_FROM_FILE)));
    }

    public void setEnableHistoryLocation( MBRequest request){
        mResourceLocator.addRequest( LocationService.class, "setEnableHistoryLocation",
                ((Boolean) request.get(Constants.LOCATION_HISTORY)));
    }

    public void setLocationSettings( MBRequest request){
        Boolean writeReadFromFile = (Boolean) request.get(Constants.LOCATION_WRITE_READ_DATA_FROM_FILE);
        if (writeReadFromFile != null) {
            mResourceLocator.addRequest( LocationService.class,"writeLocationDataToFile",
            writeReadFromFile);
        }
        Boolean enableHistory = (Boolean) request.get(Constants.LOCATION_HISTORY);
        if (enableHistory != null) {
            mResourceLocator.addRequest( LocationService.class,"setEnableHistoryLocation",
                    enableHistory);
        }
        Integer maxNumberHistoryLocations = (Integer) request.get(Constants.LOCATION_MAX_HISTORY);
        if (maxNumberHistoryLocations != null)
        {
            mResourceLocator.addRequest( LocationService.class,"setMaxNumHistoryLocations",
                        maxNumberHistoryLocations);
        }
     }

    public void setLocation( MBRequest request)
    {
        String place = (String) request.get(Constants.LOCATION_CURRENT_PLACE);
        LocationService locationService = mResourceLocator.lookupService(LocationService.class);
        if (place != null && !place.isEmpty()) {
            mResourceLocator.addRequest( LocationService.class,"setDefaultCurrentPlace",
                    place);
        } else {
            mResourceLocator.addRequest( LocationService.class,"getCurrentLocationByCoordinates",
                    ((Double) request.get(Constants.LOCATION_LATITUDE)),
                    ((Double) request.get(Constants.LOCATION_LONGITUDE)));
        }
     }

    public void getCurrentLocationByCoordinates( MBRequest request){
        mResourceLocator.addRequest(LocationService.class,"getCurrentLocationByCoordinates",
                ((Double) request.get(Constants.LOCATION_LATITUDE)),
                ((Double) request.get(Constants.LOCATION_LONGITUDE)));
    }

    public void setDefaultCurrentPlace( MBRequest request){
        mResourceLocator.addRequest(LocationService.class,"setDefaultCurrentPlace",
                (String) request.get(Constants.LOCATION_CURRENT_PLACE));
    }

    public void resetCurrentLocation( MBRequest request){
        mResourceLocator.addRequest(LocationService.class,"resetCurrentLocation");
    }

    public LocationEvent getLocation(MBRequest request){
        String place = (String) request.get(Constants.LOCATION_PLACE_NAME);
        LocationService locationService = mResourceLocator.lookupService(LocationService.class);
        if (place != null && !place.isEmpty()) {
            if (place == Constants.LOCATION_CURRENT_PLACE) {
                return locationService.fillEvent(locationService.obtainCurrentLocation());
            } else {
                return locationService.fillEvent(locationService.getPlaceLocation(
                        new LocationVO(place), true));
            }
        } else {
            Double latitude = Double.valueOf((String) request.get(Constants.LOCATION_LATITUDE));
            Double longitude = Double.valueOf((String) request.get(Constants.LOCATION_LONGITUDE));
            if (latitude != null && longitude != null) {
                return locationService.fillEvent(locationService.getPlaceLocation(
                        new LocationVO(latitude, longitude), true));
            }
        }
        return null;
    }


    public ArrayList<LocationEvent> getHistoryLocations(MBRequest mbRequest) {
        return mResourceLocator.lookupService(LocationService.class).getHistoryLocations();
    }
}
