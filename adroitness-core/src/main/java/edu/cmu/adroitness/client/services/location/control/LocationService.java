package edu.cmu.adroitness.client.services.location.control;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import com.aware.Aware_Preferences;
import com.aware.Locations;
import com.aware.providers.Locations_Provider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.reflect.TypeToken;
import edu.cmu.adroitness.comm.location.model.LocationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;
import edu.cmu.adroitness.client.services.generic.control.GenericService;
import edu.cmu.adroitness.client.services.location.model.LocationVO;
import edu.cmu.adroitness.client.services.location.model.WoeidVO;
import edu.cmu.adroitness.client.services.location.model.YqlWoeidVO;


/**
 * Google's Locations API provider. This plugin provides the user's current location in an energy efficient way.
 * This service is based on Fused Location plugin: @link:  http://www.awareframework.com/plugin/82/
 */
public class LocationService extends GenericService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Cursor locationData;
    private LocationVO mCurrentLocation;
    private LocationVO mLastCurrentLocation;
    private String defaultCurrentPlace;
    private Geocoder geocoder;
    private final String DIRECTORY = "Adroitness/location";
    private final String LAST_LOCATION_FILE_NAME = "LastLocation";
    private final String LOCATIONS_FILE_NAME = "Locations";
    private final String HISTORY_LOCATIONS_FILE_NAME = "HistoryLocations";
    private Boolean isWriteReadLocationData = true;
    private static HashMap<String, LocationVO> locations;
    private static ArrayList<LocationVO> historyLocations;
    private Boolean enableHistoryLocation = true;
    private Integer SIZE_HISTORY = 500;
    private Boolean isSaveData = true;


    public LocationService() {
        super(LocationService.class.getPackage().getName());
        if (actions.isEmpty()) {
            this.actions.add(Locations.ACTION_AWARE_LOCATIONS);
        }
        try {
            Type type = new TypeToken<HashMap<String, LocationVO>>() {
            }.getType();
            locations = Util.readObjectFromJsonFile(DIRECTORY, LOCATIONS_FILE_NAME, type);
            if (locations == null) {
                locations = new HashMap<>();
            }
            type = new TypeToken<ArrayList<LocationVO>>() {
            }.getType();
            historyLocations = Util.readObjectFromJsonFile(DIRECTORY, HISTORY_LOCATIONS_FILE_NAME, type);
            if (historyLocations == null) {
                historyLocations = new ArrayList<>();
            }
            Intent locationPlugin = new Intent(mContext, Plugin.class);
            locationPlugin.putExtra("update", true);
            mContext.startService(locationPlugin);
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
            geocoder = new Geocoder(mContext, Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LocationVO obtainCurrentLocation() {
        return Util.executeSync(new Callable<LocationVO>() {
            @Override
            public LocationVO call() throws Exception {
                if ((isWriteReadLocationData && mCurrentLocation != null && mLastCurrentLocation != null
                        && mCurrentLocation.getLatitude() != mLastCurrentLocation.getLatitude()
                        && mCurrentLocation.getLongitude() != mLastCurrentLocation.getLongitude())
                        || mLastCurrentLocation == null) {
                    mLastCurrentLocation = mCurrentLocation;
                }
                mCurrentLocation = null;

                // get current location from fused location
                getCurrentLocationByGoogleFused();
                if (mCurrentLocation == null) {
                    // get last current location from phone's sd card
                    if (isWriteReadLocationData && !historyLocations.isEmpty()) {
                        mCurrentLocation = historyLocations.get(historyLocations.size() - 1);
                    }
                    // get current location either from woeid or from default current place (the latter uses
                    // google fused location)
                    if (mCurrentLocation == null && defaultCurrentPlace != null
                            && !defaultCurrentPlace.isEmpty()) {
                        try {
                            getCurrentLocationByWoeid(getWoeidOfPlace(new LocationVO(defaultCurrentPlace)));
                            Log.d("obtainCurrentLocation",defaultCurrentPlace);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (mCurrentLocation == null) {
                        mb.send(LocationService.this,
                                LocationEvent.build().setErrorMessage("Location (GPS and Networks) " +
                                        "is disabled"));
                    }
                }
                addLocationToCache(mCurrentLocation);
                addLocationToHistory();
                return mCurrentLocation;
            }
        });
    }

    private void addLocationToCache(LocationVO locationVO) {
        if( locationVO == null ) return;
        if( !locations.containsKey( getKey( locationVO )) ){
            locations.put(locationVO.getKey(), locationVO);
        }else if( isLocationDataComplete( locationVO ) ){
            locations.remove( locationVO.getKey() );
            locations.put( locationVO.getKey() , locationVO);
        }
    }


    private void addLocationToHistory( ){
        if(enableHistoryLocation) {
            if( mCurrentLocation != null ) {
                mCurrentLocation.setTimestamp(System.currentTimeMillis());
                addLocToHistory(mCurrentLocation);
            }
        }
        if( historyLocations.size() > SIZE_HISTORY ){
            historyLocations.remove(0);
        }
    }

    private void addLocToHistory(LocationVO locationVO){
        if( locationVO != null && locationVO.getKey() != null && ( historyLocations.isEmpty() ||
            !historyLocations.get(historyLocations.size()-1).getAddress().equals(locationVO.getAddress()) )){
            historyLocations.add( locationVO );
        }
    }

    private boolean isLocationDataComplete( LocationVO locationVO ){
        if( locationVO == null ) return false;
        if( locationVO.getCountry() == null
                || locationVO.getCountryCode() == null
                || locationVO.getState() == null
                || locationVO.getStateCode() == null
                || locationVO.getCity() == null
                || locationVO.getTown() == null
                || locationVO.getSubArea() == null
                || locationVO.getPlaceName() == null
                || locationVO.getAddress() == null
                || locationVO.getLatitude() == null
                || locationVO.getLongitude() == null
                || locationVO.getZipcode() == null
                || locationVO.getTimezone() == null
                || locationVO.getWoeid() == null
                || locationVO.getAccuracy() == null
                || locationVO.getAltitude() == null
                || locationVO.getSpeed() == null
                || locationVO.getBearing() == null
                || locationVO.getWoeidVO() == null){
            return false;
        }
        return true;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Util.execute( new Runnable(){
            @Override
            public void run(){
                if( intent != null && intent.hasExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED) ) {
                    Location bestLocation = (Location) intent.getExtras().get(FusedLocationProviderApi
                            .KEY_LOCATION_CHANGED);
                    if( bestLocation == null ) return;
                    ContentValues rowData = new ContentValues();
                    rowData.put(Locations_Provider.Locations_Data.TIMESTAMP, System.currentTimeMillis());
                    rowData.put(Locations_Provider.Locations_Data.DEVICE_ID, AwareServiceWrapper.getSetting(
                            LocationService.this, Aware_Preferences.DEVICE_ID));
                    rowData.put(Locations_Provider.Locations_Data.LATITUDE, bestLocation.getLatitude());
                    rowData.put(Locations_Provider.Locations_Data.LONGITUDE, bestLocation.getLongitude());
                    rowData.put(Locations_Provider.Locations_Data.BEARING, bestLocation.getBearing());
                    rowData.put(Locations_Provider.Locations_Data.SPEED, bestLocation.getSpeed());
                    rowData.put(Locations_Provider.Locations_Data.ALTITUDE, bestLocation.getAltitude());
                    rowData.put(Locations_Provider.Locations_Data.PROVIDER, bestLocation.getProvider());
                    rowData.put(Locations_Provider.Locations_Data.ACCURACY, bestLocation.getAccuracy());
                    getContentResolver().insert(Locations_Provider.Locations_Data.CONTENT_URI, rowData);
                    mb.send(LocationService.this, fillEvent( bestLocation ) );
                }
            }
        });
    }

    public LocationEvent fillEvent( final Location bestLocation ) {
        return Util.executeSync(new Callable<LocationEvent>() {
            @Override
            public LocationEvent call() throws Exception {
                getGeoCoordinates(getLocationCursor());
                addLocationToCache(mCurrentLocation);
                addLocationToHistory();
                saveData();
                return fillEvent(mCurrentLocation)
                        .setLocationCursor(locationData)
                        .setGoogleLocation(bestLocation);
            }
        });
    }

    public LocationEvent fillEvent( final LocationVO location ){
        return Util.executeSync(new Callable<LocationEvent>() {
            @Override
            public LocationEvent call() throws Exception {
                LocationVO locationVO = location;
                if (locationVO == null) {
                    locationVO = new LocationVO();
                }
                return LocationEvent.build()
                        .setAccuracy(locationVO.getAccuracy())
                        .setAltitude(locationVO.getAltitude())
                        .setBearing(locationVO.getBearing())
                        .setLatitude(locationVO.getLatitude())
                        .setLongitude(locationVO.getLongitude())
                        .setAddress(locationVO.getAddress())
                        .setCity(locationVO.getCity())
                        .setCountryCode(locationVO.getCountryCode())
                        .setCountry(locationVO.getCountry())
                        .setPlaceName(locationVO.getPlaceName())
                        .setSpeed(locationVO.getSpeed())
                        .setState(locationVO.getState())
                        .setStateCode(locationVO.getStateCode())
                        .setSubArea(locationVO.getSubArea())
                        .setTimezone(locationVO.getTimezone())
                        .setWoeid(locationVO.getWoeid())
                        .setTown(locationVO.getTown())
                        .setZipcode(locationVO.getZipcode())
                        .setTimestamp(locationVO.getTimestamp());
            }
        });
    }


    public void setDefaultCurrentPlace(String place) {
        defaultCurrentPlace = place;
    }


    public void getCurrentLocationByCoordinates(final Double latitude, final Double longitude) {
        Util.execute(new Runnable() {
            public void run() {
                getCurrentLocationByWoeid(new WoeidVO(latitude, longitude));
            }
        });
    }

    public void setCurrentLocationVO(LocationVO locationVO){
        if( mCurrentLocation != null ){
            mLastCurrentLocation = mCurrentLocation;
        }
        mCurrentLocation = locationVO;
    }

    public void getCurrentLocationByGoogleFused( ){
        Util.execute(new Runnable() {
            public void run() {
                try {
                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(Plugin.mLocationClient);
                    if (mLastLocation != null) {
                        double latitude = mLastLocation.getLatitude();
                        double longitude = mLastLocation.getLongitude();
                        getCurrentLocationByWoeid(new WoeidVO(latitude, longitude));

                    }
                }catch(SecurityException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getCurrentLocationByWoeid(final WoeidVO woeidVO) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Double latitude = woeidVO.getLatitude(), longitude = woeidVO.getLongitude();
                try {
                    if (isLocationDataComplete(locations.get(getKey(new LocationVO(latitude, longitude))))) {
                        mCurrentLocation = locations.get(mCurrentLocation.getKey());
                    } else {
                        List<Address> addresses = null;
                        if (latitude != null && longitude != null) {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } else if (defaultCurrentPlace != null && woeidVO.isFetched()) {
                            addresses = geocoder.getFromLocationName(defaultCurrentPlace, 1);
                        }
                        if (addresses != null && addresses.size() > 0) {
                            setCurrentLocationVO(setLocationFromAddress(mCurrentLocation, addresses.get(0),
                                    woeidVO, true));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private LocationVO setLocationFromAddress(final LocationVO location, final Address address,
                                              final WoeidVO woeid, final Boolean isCurrentLocation) {
        return Util.executeSync(new Callable<LocationVO>() {
            @Override
            public LocationVO call() throws Exception {
                LocationVO locationVO = location;
                WoeidVO woeidVO = woeid;
                if (locationVO == null) {
                    locationVO = new LocationVO();
                }
                if (woeidVO == null) {
                    woeidVO = locationVO.getWoeidVO();
                }
                if (woeidVO.getWoeid() == null) {
                    locationVO.setSubArea(address.getLocality());
                    locationVO.setZipcode(address.getPostalCode());
                    locationVO.setState(address.getAdminArea());
                    String addressLine ="";
                    for(int i=0;i<=address.getMaxAddressLineIndex();i++)
                    {

                        if(i<=address.getMaxAddressLineIndex()-1)
                        {
                            addressLine += address.getAddressLine(i)+",";
                        }
                        else
                        {
                            addressLine += address.getAddressLine(i);
                        }
                    }
                    if(address.getMaxAddressLineIndex()>0) {
                        locationVO.setAddress(addressLine);
                        Log.d("address", addressLine);
                    }
                    woeidVO = getWoeidOfPlace(locationVO);
                    Log.d("setLocationFromAddress",locationVO.getAddress()+locationVO.getSubArea());
                }

                locationVO.setCountry(address.getCountryName());
                locationVO.setState(address.getAdminArea());
                locationVO.setCity(address.getLocality());
                locationVO.setCountryCode(address.getCountryCode());

                String stateCode = "";
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    stateCode = address.getAddressLine(i);
                    if (stateCode.startsWith(address.getLocality() + ", ")) {
                        stateCode = stateCode.substring(stateCode.indexOf(",") + 2,
                                stateCode.indexOf(",") + 4);
                        break;
                    }
                }

                locationVO.setStateCode(stateCode);
                locationVO.setZipcode(address.getPostalCode());
                locationVO.setLatitude(address.getLatitude());
                locationVO.setLongitude(address.getLongitude());
                if (woeidVO != null && woeidVO.getName() != null && !woeidVO.getName().isEmpty()) {
                    locationVO.setPlaceName(woeidVO.getName());
                } else {
                    locationVO.setPlaceName(address.getFeatureName());
                }
                locationVO.setAddress(((address.getSubThoroughfare() == null ? "" : address.getSubThoroughfare())
                        + " " + (address.getThoroughfare() == null ? "" : address.getThoroughfare())).trim());
                if (locationVO.getSubArea() == null) {
                    locationVO.setSubArea(address.getLocality());
                    if (locationVO.getSubArea() == null) {
                        locationVO.setSubArea(address.getSubAdminArea());
                    }
                }
                if (locationVO.getCountry() == null || locationVO.getCountry().equals("")) {
                    mb.send(LocationService.this,
                            LocationEvent.build().setErrorMessage("Couldn't find your location, try later"));
                }

                if (isCurrentLocation) {
                    if (locationVO.getPlaceName() != null && !locationVO.getPlaceName().isEmpty()) {
                        defaultCurrentPlace = locationVO.getPlaceName();
                    } else if (locationVO.getSubArea() != null && !locationVO.getSubArea().isEmpty()) {
                        defaultCurrentPlace = locationVO.getSubArea();
                    } else if (locationVO.getTown() != null && !locationVO.getTown().isEmpty()) {
                        defaultCurrentPlace = locationVO.getTown();
                    } else if (locationVO.getCity() != null && !locationVO.getCity().isEmpty()) {
                        defaultCurrentPlace = locationVO.getCity();
                    }
                }
                if (locationVO.getWoeidVO() == null) {
                    locationVO.setWoeidVO(woeidVO);
                }
                return locationVO;
            }
        });
    }

    public Cursor getLocationCursor(){
        return Util.executeSync(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                if (locationData == null) {
                    locationData = mContext.getContentResolver().query(
                            Locations_Provider.Locations_Data.CONTENT_URI,
                            new String[]{Locations_Provider.Locations_Data.LATITUDE,
                                    Locations_Provider.Locations_Data.LONGITUDE,
                                    Locations_Provider.Locations_Data.ALTITUDE,
                                    Locations_Provider.Locations_Data.ACCURACY,
                                    Locations_Provider.Locations_Data.BEARING,
                                    Locations_Provider.Locations_Data.SPEED}, null, null, null);
                }
                return locationData;
            }
        });
    }

    public void getGeoCoordinates(final Cursor locationCursor){
//        Util.execute( new Runnable(){
//            @Override
//            public void run(){
//                if (locationCursor != null && locationCursor.moveToLast()) {
//                    // Here we read the value
//                    mCurrentLocation.setLatitude(locationCursor.getDouble(locationCursor.getColumnIndex(Locations_Data
//                            .LATITUDE)));
//                    mCurrentLocation.setLongitude(locationCursor.getDouble(locationCursor.getColumnIndex(Locations_Data
//                            .LONGITUDE)));
//                    if( isLocationDataComplete( mCurrentLocation ) ){
//                        mCurrentLocation = locations.get( mCurrentLocation.getKey() );
//                    }else {
//                        getCurrentLocationByCoordinates(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                        mCurrentLocation.setAltitude(locationCursor.getDouble(locationCursor.getColumnIndex(
//                                Locations_Data.ALTITUDE)));
//                        mCurrentLocation.setSpeed(locationCursor.getDouble(locationCursor.getColumnIndex(
//                                Locations_Data.SPEED)));
//                        mCurrentLocation.setBearing(locationCursor.getFloat(locationCursor.getColumnIndex(
//                                Locations_Data.BEARING)));
//                        mCurrentLocation.setAccuracy(locationCursor.getFloat(locationCursor.getColumnIndex(
//                                Locations_Data.ACCURACY)));
//                    }
//                }
//            }
//        });
    }


    public WoeidVO getWoeidOfPlace(final LocationVO locationVO ){
        return Util.executeSync(new Callable<WoeidVO>() {
            @Override
            public WoeidVO call() throws Exception {
                final String place = locationVO.getSubArea() != null && !locationVO.getSubArea().isEmpty() ?
                        locationVO.getSubArea() : locationVO.getCity();
                if (place == null || place.isEmpty()) {
                    return null;
                }
                final WoeidVO woeidVOTemp = new WoeidVO(place);
                YqlWoeidVO yqlWoeidVO = new YqlWoeidVO();
                try {
                    HttpURLConnection urlConnection = null;
                    try {
                        URL url = new URL(String.format(UtilServiceAPIs.QUERY_WOEID,
                                URLEncoder.encode(place, "UTF-8")));
                        String urlString;
                        if(locationVO.getAddress()!=null)
                        {

                                Log.d("getAddress", locationVO.getAddress());
                                urlString = UtilServiceAPIs.API_YQL_LOCATION_QUERY.replace("location_name",
                                        URLEncoder.encode(locationVO.getAddress(), "UTF-8"));
                        }
                        else
                        {
                            Log.d("getPlace", place);
                            urlString = UtilServiceAPIs.API_YQL_LOCATION_QUERY.replace("location_name",
                                    URLEncoder.encode(place, "UTF-8"));
                        }

                        //URL newUrl =new URL(String.format(UtilServiceAPIs.API_YQL_LOCATION_QUERY.replace("location_name",
                          //      URLEncoder.encode(place+","+locationVO.getZipcode(), "UTF-8")) ));
                        URL newUrl =new URL(urlString);
                        Log.d("QUERY_WOEID", newUrl.toString());
                        urlConnection = (HttpURLConnection) newUrl.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader( urlConnection.getInputStream(), "UTF-8"));
                        StringBuilder jsonResponse = new StringBuilder();
                        //parseWoeid(in, woeidVOTemp);
                        String inputJson ="";
                        while ((inputJson = in.readLine())!=null)
                        {
                            jsonResponse.append(inputJson);
                        }
                        woeidVOTemp.setFetched(true);
                        //new JsonParser().parse(jsonResponse.toString()).deepCopy().getAsJsonObject().get("query").getAsJsonObject().get("results").getAsJsonObject().get("place").getAsJsonArray()
                        yqlWoeidVO = Util.fromJson(jsonResponse.toString(), YqlWoeidVO.class);
                        locationVO.setYqlWoeidVO(yqlWoeidVO);
                        parseWoeidFromYQLResult(yqlWoeidVO, locationVO, woeidVOTemp);
                        in.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(urlConnection!=null) {
                            urlConnection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return woeidVOTemp;
            }
        });
    }

    private WoeidVO parseWoeid(final InputStream in, final WoeidVO woeidVO) throws Exception {
        return Util.executeSync(new Callable<WoeidVO>() {
            @Override
            public WoeidVO call() throws Exception {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    parser.nextTag();
                    HashMap<String, String> mappings = new HashMap<>();
                    mappings.put("place", "");
                    mappings.put("woeid", "setWoeid");
                    mappings.put("name", "setName");
                    mappings.put("country", "setCountry");
                    mappings.put("country.code", "setCountryCode");
                    mappings.put("admin1", "setState");
                    mappings.put("admin1.code", "setStateCode");
                    mappings.put("admin2", "setCounty");
                    mappings.put("locality1", "setTown");
                    mappings.put("postal", "setZipcode");
                    mappings.put("centroid", "");
                    mappings.put("latitude", "setLatitude");
                    mappings.put("longitude", "setLongitude");
                    mappings.put("timezone", "setTimezone");
                    return Util.readXMLToObject(parser, "places", mappings, woeidVO);
                } finally {
                    in.close();
                }
            }
        });
    }
    WoeidVO woeidVO;
    private WoeidVO parseWoeidFromYQLResult(final YqlWoeidVO yqlWoeidVO, final LocationVO locationVO, final WoeidVO woeidVOTemp) throws Exception {
        return Util.executeSync(new Callable<WoeidVO>() {
            @Override
            public WoeidVO call() throws Exception {
                try {
                     //woeidVO = new WoeidVO();
                    YqlWoeidVO.Query query = yqlWoeidVO.getQuery();
                    YqlWoeidVO.Results results = query.getResults();
                    YqlWoeidVO.Place placeObj = results.getPlace();

                    if(placeObj.getAdmin1().getContent().equals(locationVO.getState()))
                    {
                        woeidVOTemp.setName(placeObj.getName());
                        woeidVOTemp.setWoeid(placeObj.getWoeid());
                        woeidVOTemp.setCountry(placeObj.getCountry().getContent());
                        woeidVOTemp.setCountryCode(placeObj.getCountry().getCode());
                        woeidVOTemp.setState(placeObj.getAdmin1().getContent());
                        woeidVOTemp.setStateCode(placeObj.getAdmin1().getCode());
                        woeidVOTemp.setCounty(placeObj.getAdmin2().getContent());
                        woeidVOTemp.setWoeid(placeObj.getWoeid());
                        woeidVOTemp.setTown(placeObj.getLocality1().getContent());
                        String zipCode = placeObj.getPostal()!=null? placeObj.getPostal().getContent() : "";
                        woeidVOTemp.setZipcode(zipCode);
                        woeidVOTemp.setLatitude(Double.valueOf(placeObj.getCentroid().getLatitude()));
                        woeidVOTemp.setLongitude(Double.valueOf(placeObj.getCentroid().getLongitude()));
                        woeidVOTemp.setTimezone(placeObj.getTimezone().getContent());
                    }

                    //woeidVO.setCountry(
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return woeidVOTemp;
            }
        });
    }


    public LocationVO getPlaceLocation(String place, Boolean checkWoeid){
        return getPlaceLocation( new LocationVO( place ), checkWoeid );
    }

    public LocationVO getPlaceLocation(Double latitude, Double longitude, Boolean checkWoeid){
        return getPlaceLocation( new LocationVO( latitude, longitude ), checkWoeid );
    }

    public LocationVO getPlaceLocation(final LocationVO location, final Boolean checkWoeid){
        return Util.executeSync(new Callable<LocationVO>() {
            @Override
            public LocationVO call() throws Exception {
                // make sure locationVO has no null values AND LocationVO has to contain at least the city
                // or the subarea
                LocationVO locationVO = location;
                if( locationVO == null || ( locationVO.getSubArea() == null && locationVO.getCity() == null
                        && locationVO.getLatitude() == null && locationVO.getLongitude() == null) ){
                    mb.send(LocationService.this,
                            LocationEvent.build().setErrorMessage("Place is not valid (it is null or empty)"));
                    return null;
                }
                try {
                    List<Address> addresses = null;
                    // if we have latitude and longitude
                    if (locationVO.getSubArea() == null && locationVO.getCity() == null &&
                            locationVO.getLatitude() != null && locationVO.getLongitude() != null) {
                        if( isLocationDataComplete( locations.get( getKey(locationVO ) ) ) ){
                            return locations.get( locationVO.getKey() );
                        }
                        addresses = geocoder.getFromLocation(locationVO.getLatitude(), locationVO.getLongitude(), 1);
                        if( addresses != null && !addresses.isEmpty() ) {
                            locationVO.setSubArea(addresses.get(0).getLocality() );
                        }
                    }
                    // WOEID (latitude, longitude, name of place, etc)
                    if ( checkWoeid && locationVO.getWoeidVO() == null) {
                        locationVO.setWoeidVO( getWoeidOfPlace(locationVO) );
                        Log.d("checkWoeid",locationVO.getAddress()+locationVO.getSubArea());
                    }
                    // If the location place is LOCATION_CURRENT_PLACE then use the current location
                    // (GPS or Networks)
                    if ((locationVO.getCity() != null && locationVO.getCity().equals(
                            Constants.LOCATION_CURRENT_PLACE)) || (locationVO.getSubArea() != null
                            && locationVO.getSubArea().equals(Constants.LOCATION_CURRENT_PLACE))) {
                        locationVO = obtainCurrentLocation();
                    } else if( addresses == null || addresses.isEmpty() ){
                        addresses = geocoder.getFromLocationName(locationVO.getPlace(), 1);
                    }
                    // fill in all the LocationVO attributes
                    if (addresses != null && addresses.size() > 0) {
                        if( isLocationDataComplete( locations.get( getKey( locationVO ) ) ) ){
                            return locations.get( locationVO.getKey() );
                        }
                        locationVO = setLocationFromAddress(locationVO, addresses.get(0), null, false);
                    }
                }catch (Exception e){
                    //e.printStackTrace();
                }
                addLocationToCache( locationVO );
                return locationVO;
            }
        });
    }

    public void resetCurrentLocation(){
        setCurrentLocationVO( null );
        mLastCurrentLocation = null;
        defaultCurrentPlace = null;
    }

    public void resetHistoryLocations(){
        historyLocations.clear();
    }

    public void resetLocations(){
        locations.clear();
    }

    public void writeLocationDataToFile( final Boolean isWrite ){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                LocationService.this.isWriteReadLocationData = isWrite;
                if( !isWriteReadLocationData){
                    Util.removeFile( DIRECTORY, LAST_LOCATION_FILE_NAME);
                }
            }
        });
    }

    public void setEnableHistoryLocation(Boolean enableHistoryLocation){
        this.enableHistoryLocation = enableHistoryLocation;
    }

    public ArrayList<LocationEvent> getHistoryLocations(){
        return Util.executeSync( new Callable<ArrayList<LocationEvent>>(){
            @Override
            public ArrayList<LocationEvent> call() throws Exception{
                ArrayList<LocationEvent> history = new ArrayList<>();
                for( LocationVO locationVO : historyLocations ){
                    history.add( fillEvent( locationVO ) );
                }
                return history;
            }
        });
    }

    private String getKey( LocationVO locationVO ){
        if( locationVO == null || locationVO.getLatitude() ==  null || locationVO.getLongitude() == null){
            return "";
        }
        if( locationVO.getKey() == null ){
            locationVO.setKey( locationVO.getLatitude() + ", " + locationVO.getLongitude() );
        }
        return locationVO.getKey();
    }

    public void setMaxNumHistoryLocations( Integer maxNumHistoryLocations ){
        if( maxNumHistoryLocations > 0 ) {
            SIZE_HISTORY = maxNumHistoryLocations;
        }
    }

    @Override
    public void doAfterBind() {
        getCurrentLocationByGoogleFused();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    private void saveData(){
        if( isSaveData ) {
            Util.execute(new Runnable() {
                @Override
                public void run() {
                    String directory = DIRECTORY; //Environment.DIRECTORY_DOCUMENTS;
                    if (isWriteReadLocationData) {
                        Util.writeObjectToJsonFile(mCurrentLocation != null ? mCurrentLocation
                                : mLastCurrentLocation, directory, LAST_LOCATION_FILE_NAME);
                    }
                    if (locations != null) {
                        Util.writeObjectToJsonFile(locations, DIRECTORY, LOCATIONS_FILE_NAME);
                    }
                    if (historyLocations != null) {
                        Util.writeObjectToJsonFile(historyLocations, DIRECTORY, HISTORY_LOCATIONS_FILE_NAME);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy(){
        saveData();
        setCurrentLocationVO( null );
        mLastCurrentLocation = null;
        mCurrentLocation = null;
        if( locationData != null ) {
            locationData.close();
            locationData = null;
        }
        super.onDestroy();
    }
}
