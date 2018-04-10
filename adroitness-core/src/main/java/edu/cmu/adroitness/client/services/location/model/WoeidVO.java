package edu.cmu.adroitness.client.services.location.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by oscarr on 9/10/15.
 * Yahoo WOEID: Where On Earth ID
 */
public class WoeidVO extends DataObject {
    private String name;
    private String woeid;
    private String country;
    private String state;
    private String county;
    private String town;
    private String zipcode;
    private String timezone;
    private Double latitude;
    private Double longitude;
    private String stateCode;
    private String countryCode;
    private boolean fetched = false;


    public WoeidVO(){}

    public WoeidVO(String placeName) {
        this.name = placeName;
    }

    public WoeidVO( Double latitude, Double longitude ) {
        if( latitude != null ) this.latitude = latitude;
        if( longitude != null ) this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.valueOf(latitude);
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.valueOf(longitude);
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        if( stateCode.contains("-") ) {
            this.stateCode = stateCode.substring( stateCode.indexOf("-") + 1 );
        }else{
            this.stateCode = stateCode;
        }
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }
}
