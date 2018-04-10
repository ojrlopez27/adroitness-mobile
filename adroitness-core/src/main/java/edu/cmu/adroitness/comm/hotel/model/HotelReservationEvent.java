package edu.cmu.adroitness.comm.hotel.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.booking.model.HotelVO;

import java.util.List;

/**
 * Created by oscarr on 8/12/15.
 */
public class HotelReservationEvent extends BaseEvent {
    private String errorMessage;
    private List<HotelVO> hotels;

    private HotelReservationEvent(){ super(); }
    private HotelReservationEvent(int mbRequestId){ super( mbRequestId); }

    public static HotelReservationEvent build(){
        return new HotelReservationEvent();
    }
    public static HotelReservationEvent build(int mbRequestId){
        return new HotelReservationEvent(mbRequestId);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HotelReservationEvent setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public List<HotelVO> getHotels() {
        return hotels;
    }

    public HotelReservationEvent setHotels(List<HotelVO> hotels) {
        this.hotels = hotels;
        return this;
    }
}
