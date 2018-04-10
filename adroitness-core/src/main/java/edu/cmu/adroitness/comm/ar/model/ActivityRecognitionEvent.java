package edu.cmu.adroitness.comm.ar.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 8/12/15.
 */
public class ActivityRecognitionEvent extends BaseEvent {
    private int activityType;
    private int confidence;

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
}
