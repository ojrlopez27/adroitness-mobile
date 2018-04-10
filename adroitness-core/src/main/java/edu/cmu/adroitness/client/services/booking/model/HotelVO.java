package edu.cmu.adroitness.client.services.booking.model;

import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.ArrayList;

/**
 * Created by oscarr on 8/3/15.
 */
public class HotelVO extends DataObject implements Util.WritableJsonObject{

    private String url;
    private Double totalPrice;
    private ArrayList<String> amenityCodes;
    private Double pricePerNight;
    private Double recommendationPercentage;
    private Double savingPercentage;
    private String starRating;

    public String toString() {
        String string = "Url = " + url + "\n" +
                "TotalPrice = " + totalPrice + "\n" +
                "Amenities = " + amenityCodes.toString() + "\n" +
                "Price per night = " + pricePerNight + "\n" +
                "Recommendation = " + recommendationPercentage + "%\n" +
                "Saving = " + savingPercentage + "%\n" +
                "Star = " + starRating;
        return string;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = Double.valueOf(totalPrice);
    }

    public ArrayList<String> getAmenityCodes() {
        return amenityCodes;
    }

    public void setAmenityCodes(ArrayList<String> amenityCodes) {
        this.amenityCodes = amenityCodes;
    }

    public void setAmenityCode(String amenityCode) {
        if( this.amenityCodes == null ){
            this.amenityCodes = new ArrayList<>();
        }
        this.amenityCodes.add( amenityCode );
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(String pricePerNight) {
        this.pricePerNight = Double.valueOf(pricePerNight);
    }

    public Double getRecommendationPercentage() {
        return recommendationPercentage;
    }

    public void setRecommendationPercentage(String recommendationPercentage) {
        this.recommendationPercentage = Double.valueOf(recommendationPercentage);
    }

    public Double getSavingPercentage() {
        return savingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = Double.valueOf(savingPercentage);
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    @Override
    public void clean(){
        url = null;
        totalPrice = null;
        amenityCodes = new ArrayList();
        pricePerNight = null;
        recommendationPercentage = null;
        savingPercentage = null;
        starRating = null;
    }
}
