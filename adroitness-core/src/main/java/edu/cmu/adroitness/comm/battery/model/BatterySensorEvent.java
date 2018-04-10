package edu.cmu.adroitness.comm.battery.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 8/13/15.
 */
public class BatterySensorEvent extends BaseEvent {
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
