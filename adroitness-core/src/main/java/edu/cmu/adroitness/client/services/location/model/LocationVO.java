package edu.cmu.adroitness.client.services.location.model;

import org.apache.commons.lang3.StringUtils;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by oscarr on 8/5/15.
 */
public class LocationVO extends DataObject {
    private String country;
    private String state;
    private String city;
    private String subArea;
    private String town;
    private String countryCode;
    private String stateCode;
    private String woeid;
    private String zipcode;
    private String timezone;

    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Float accuracy;
    private Float bearing;
    private Double speed;
    private String placeName;
    private String address;
    /** Yahoo Where On Earth ID **/
    private WoeidVO woeidVO;
    private YqlWoeidVO yqlWoeidVO;
    private long timestamp;
    private String key;





    public LocationVO() {}
    public LocationVO(String place) {
        if( place != null && !place.equals("") ) {
            this.subArea = place.replaceAll("\\s+", "_").trim();
        }
    }

    public LocationVO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        if( country == null && woeidVO != null ){
            setCountry(woeidVO.getCountry());
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country != null? country.replaceAll("\\s+", "_").trim() : null;
    }

    public String getCity() {
        if( city == null && woeidVO != null ){
            setCity(woeidVO.getTown());
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city != null && !city.trim().equals("")?
                city.replaceAll("\\s+", "_").trim() : null;
    }

    public String getSubArea() {
        if( subArea == null && woeidVO != null ){
            setSubArea(woeidVO.getName());
        }
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea != null && !subArea.trim().equals("")?
                subArea.replaceAll("\\s+", "_").trim() : null;
    }

    public String getStateCode() {
        if( stateCode == null && woeidVO != null ){
            setStateCode(woeidVO.getStateCode());
        }
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode != null? stateCode.replaceAll("\\s+", "_").trim() : null;
    }

    public String getCountryCode() {
        if( countryCode == null && woeidVO != null ){
            setCountryCode(woeidVO.getCountryCode());
        }
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode != null? countryCode.replaceAll("\\s+", "_").trim() : null;
    }


    public String getState() {
        if( state == null && woeidVO != null ){
            setState(woeidVO.getState());
        }
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTown() {
        if( town == null && woeidVO != null ){
            setTown(woeidVO.getTown());
        }
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getWoeid() {
        if( woeid == null && woeidVO != null ){
            setWoeid(woeidVO.getWoeid());
        }
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getZipcode() {
        if( zipcode == null && woeidVO != null ){
            setZipcode(woeidVO.getZipcode());
        }
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTimezone() {
        if( timezone == null && woeidVO != null ){
            setTimezone(woeidVO.getTimezone());
        }
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Double getLatitude() {
        if( latitude == null && woeidVO != null ){
            setLatitude(woeidVO.getLatitude());
        }
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        if( longitude == null && woeidVO != null ){
            setLongitude(woeidVO.getLongitude());
        }
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Float getBearing() {
        return bearing;
    }

    public void setBearing(Float bearing) {
        this.bearing = bearing;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public WoeidVO getWoeidVO() {
        return woeidVO;
    }

    public String getPlaceName() {
        if( placeName == null && woeidVO != null ){
            setPlaceName( woeidVO.getName() );
        }
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setWoeidVO(WoeidVO woeidVO) {
        this.woeidVO = woeidVO;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void mergeLocation( LocationVO locToMerge){
        if( country == null) setCountry(locToMerge.getCountry());
        if( state == null) setState(locToMerge.getState());
        if( city  == null) setCity( locToMerge.getCity());
        if( subArea == null) setSubArea(locToMerge.getSubArea());
        if( town == null) setSubArea(locToMerge.getTown());
        if( placeName == null) setSubArea(locToMerge.getPlaceName());
        if( countryCode == null) setCountryCode(locToMerge.getCountryCode());
        if( stateCode == null) setStateCode(locToMerge.getStateCode());
    }

    public String getPlace() {
        try{
            if(woeidVO == null || (subArea != null)) {
                if (woeidVO != null && woeidVO.getTown()!=null) {
                    if (StringUtils.stripAccents(subArea).equals(woeidVO.getTown())) {
                        return subArea.replace(" ", "_").trim();
                    } else {
                        return woeidVO.getTown().replace(" ", "_").trim();
                    }
                } else
                {
                    return subArea.replace(" ", "_").trim();
                }
            }
            /*return ( (woeidVO == null ) || (subArea != null && StringUtils.stripAccents( subArea )
                    .equals(woeidVO.getTown()) )? subArea : woeidVO.getTown()).replace(" ", "_").trim();*/
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public YqlWoeidVO getYqlWoeidVO() {
        return yqlWoeidVO;
    }

    public void setYqlWoeidVO(YqlWoeidVO yqlWoeidVO) {
        this.yqlWoeidVO = yqlWoeidVO;
    }



}
