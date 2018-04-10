package edu.cmu.adroitness.effectors.alarm.model;

/**
 * Created by oscarr on 9/29/15.
 */
public class AlarmVO {

    private Long triggerAtMillis;
    private Long intervalMillis;


    public Long getTriggerAtMillis() {
        return triggerAtMillis;
    }

    public void setTriggerAtMillis(Long triggerAtMillis) {
        this.triggerAtMillis = triggerAtMillis;
    }

    public Long getIntervalMillis() {
        return intervalMillis;
    }

    public void setIntervalMillis(Long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }
}
