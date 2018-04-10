package edu.cmu.adroitness.client.services.booking.control;

import edu.cmu.adroitness.client.services.booking.model.Criteria;

import java.util.ArrayList;

/**
 * Created by oscarr on 8/3/15.
 */
public interface Booking {
    ArrayList search( Criteria criteria );
    boolean checkInputs();
}
