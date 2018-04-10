package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.booking.control.HotelReservationService;
import edu.cmu.adroitness.client.services.booking.model.HotelSearchCriteria;

/**
 * Created by oscarr on 3/15/16.
 */
public final class HotelAdapter extends ChannelAdapter {
    private static HotelAdapter instance;

    private HotelAdapter() {
        super();
    }

    public static HotelAdapter getInstance() {
        if (instance == null) {
            instance = new HotelAdapter();
        }
        return instance;
    }

    public void searchHotel( MBRequest request){
        mResourceLocator.addRequest( HotelReservationService.class, "search",
                ((HotelSearchCriteria) request.get(Constants.HOTEL_CRITERIA)));
    }
}
