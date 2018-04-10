package edu.cmu.adroitness.client.services.booking.model;

import java.util.Date;

/**
 * Created by oscarr on 8/3/15.
 */
public class HotelSearchCriteria extends Criteria {

    public static final String SORT_BY_PRICE = "price";
    private String address;
    private String adults;
    private String rooms;
    private String children;
    private Date startDate;
    private Date endDate;
    private String sortBy = SORT_BY_PRICE;
    private int resultLimit = 10;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public void setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;
    }
}
