package edu.cmu.adroitness.client.services.location.model;

/**
 * Created by sakoju on 2/16/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YqlWoeidVO {

    @SerializedName("query")
    @Expose
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

public class Admin1 {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}


public class Admin2 {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class BoundingBox {

    @SerializedName("southWest")
    @Expose
    private SouthWest southWest;
    @SerializedName("northEast")
    @Expose
    private NorthEast northEast;

    public SouthWest getSouthWest() {
        return southWest;
    }

    public void setSouthWest(SouthWest southWest) {
        this.southWest = southWest;
    }

    public NorthEast getNorthEast() {
        return northEast;
    }

    public void setNorthEast(NorthEast northEast) {
        this.northEast = northEast;
    }

}

public class Centroid {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}


public class Country {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class Locality1 {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class Locality2 {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class NorthEast {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}

public class Place {

    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("xmlns")
    @Expose
    private String xmlns;
    @SerializedName("yahoo")
    @Expose
    private String yahoo;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("placeTypeName")
    @Expose
    private PlaceTypeName placeTypeName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("admin1")
    @Expose
    private Admin1 admin1;
    @SerializedName("admin2")
    @Expose
    private Admin2 admin2;
    @SerializedName("admin3")
    @Expose
    private Object admin3;
    @SerializedName("locality1")
    @Expose
    private Locality1 locality1;
    @SerializedName("locality2")
    @Expose
    private Locality2 locality2;
    @SerializedName("postal")
    @Expose
    private Postal postal;
    @SerializedName("centroid")
    @Expose
    private Centroid centroid;
    @SerializedName("boundingBox")
    @Expose
    private BoundingBox boundingBox;
    @SerializedName("areaRank")
    @Expose
    private String areaRank;
    @SerializedName("popRank")
    @Expose
    private String popRank;
    @SerializedName("timezone")
    @Expose
    private Timezone timezone;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getYahoo() {
        return yahoo;
    }

    public void setYahoo(String yahoo) {
        this.yahoo = yahoo;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public PlaceTypeName getPlaceTypeName() {
        return placeTypeName;
    }

    public void setPlaceTypeName(PlaceTypeName placeTypeName) {
        this.placeTypeName = placeTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Admin1 getAdmin1() {
        return admin1;
    }

    public void setAdmin1(Admin1 admin1) {
        this.admin1 = admin1;
    }

    public Admin2 getAdmin2() {
        return admin2;
    }

    public void setAdmin2(Admin2 admin2) {
        this.admin2 = admin2;
    }

    public Object getAdmin3() {
        return admin3;
    }

    public void setAdmin3(Object admin3) {
        this.admin3 = admin3;
    }

    public Locality1 getLocality1() {
        return locality1;
    }

    public void setLocality1(Locality1 locality1) {
        this.locality1 = locality1;
    }

    public Locality2 getLocality2() {
        return locality2;
    }

    public void setLocality2(Locality2 locality2) {
        this.locality2 = locality2;
    }

    public Postal getPostal() {
        return postal;
    }

    public void setPostal(Postal postal) {
        this.postal = postal;
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getAreaRank() {
        return areaRank;
    }

    public void setAreaRank(String areaRank) {
        this.areaRank = areaRank;
    }

    public String getPopRank() {
        return popRank;
    }

    public void setPopRank(String popRank) {
        this.popRank = popRank;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

}

public class PlaceTypeName {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("content")
    @Expose
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class Postal {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

public class Query {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("results")
    @Expose
    private Results results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

}

public class Results {

    @SerializedName("place")
    @Expose
    private Place placeList = null;

    public Place getPlace() {
        return placeList;
    }

    public void setPlace(Place place) {
        this.placeList = place;
    }
}

public class SouthWest {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}

public class Timezone {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("woeid")
    @Expose
    private String woeid;
    @SerializedName("content")
    @Expose
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}



}