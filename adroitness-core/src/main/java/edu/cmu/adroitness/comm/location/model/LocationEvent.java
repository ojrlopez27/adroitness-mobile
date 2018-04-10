package edu.cmu.adroitness.comm.location.model;

import android.database.Cursor;
import android.location.Location;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 8/12/15.
 */
public class LocationEvent extends BaseEvent{

    private Cursor locationCursor;
    private Location googleLocation;
    private String country;
    private String state;
    private String city;
    private String subArea;
    private String town;
    private String countryCode;
    private String stateCode;
    private String placeName;
    /** Yahoo Where On Earth ID **/
    private String woeid;
    private String zipcode;
    private String timezone;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Float accuracy;
    private Float bearing;
    private Double speed;
    private String address;
    private String errorMessage;
    private Long timestamp;



    private LocationEvent(){ super(); }
    private LocationEvent(int mbRequestId){ super( mbRequestId); }

    public static LocationEvent build(){
        return new LocationEvent();
    }
    public static LocationEvent build(int mbRequestId){
        return new LocationEvent(mbRequestId);
    }

    public Cursor getLocationCursor() {
        return locationCursor;
    }

    public LocationEvent setLocationCursor(Cursor locationCursor) {
        this.locationCursor = locationCursor;
        return this;
    }

    public Double getLatidude() {
        return latitude;
    }

    public LocationEvent setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationEvent setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocationEvent setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Float getBearing() {
        return bearing;
    }

    public LocationEvent setBearing(Float bearing) {
        this.bearing = bearing;
        return this;
    }

    public Double getAltitude() {
        return altitude;
    }

    public LocationEvent setAltitude(Double altitude) {
        this.altitude = altitude;
        return this;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public LocationEvent setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public Location getGoogleLocation() {
        return googleLocation;
    }

    public LocationEvent setGoogleLocation(Location googleLocation) {
        this.googleLocation = googleLocation;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public LocationEvent setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getState() {
        return state;
    }

    public LocationEvent setState(String state) {
        this.state = state;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LocationEvent setCity(String city) {
        this.city = city;
        return this;
    }

    public String getSubArea() {
        return subArea;
    }

    public LocationEvent setSubArea(String subArea) {
        this.subArea = subArea;
        return this;
    }

    public String getTown() {
        return town;
    }

    public LocationEvent setTown(String town) {
        this.town = town;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public LocationEvent setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getStateCode() {
        return stateCode;
    }

    public LocationEvent setStateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public String getPlaceName() {
        return placeName;
    }

    public LocationEvent setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getWoeid() {
        return woeid;
    }

    public LocationEvent setWoeid(String woeid) {
        this.woeid = woeid;
        return this;
    }

    public String getZipcode() {
        return zipcode;
    }

    public LocationEvent setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public LocationEvent setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public LocationEvent setSpeed(Double speed) {
        this.speed = speed;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public LocationEvent setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LocationEvent setAddress(String address) {
        this.address = address;
        return this;
    }
}
