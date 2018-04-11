package edu.cmu.adroitness.client.services.hotel.control;

import android.app.Activity;

import java.util.ArrayList;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.booking.model.HotelSearchCriteria;
import edu.cmu.adroitness.client.services.booking.model.HotelVO;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;


/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    protected MessageBroker mMB;
    protected Activity mActivity;

    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( activity );
        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }


    // ****************************** HOTEL ***********************************************

    public ArrayList<HotelVO> searchHotel(HotelSearchCriteria criteria){
        return (ArrayList<HotelVO>)mMB.get(ViewHelper.this,
                MBRequest.build(Constants.MSG_SEARCH_HOTEL)
                .put(Constants.HOTEL_CRITERIA, criteria));
    }


}
