package edu.cmu.adroitness.comm.accelerometer.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 8/13/15.
 */
public class AccelerometerEvent extends BaseEvent {
    /**
     * From rowData you can extract:
     * "device_id"
     * "timestamp"
     * "double_sensor_maximum_range"
     * "double_sensor_minimum_delay"
     * "sensor_name"
     * "double_sensor_power_ma"
     * "double_sensor_resolution"
     * "sensor_type"
     * "sensor_vendor"
     * "sensor_version"
     */
    private Float accelerationX;
    private Float accelerationY;
    private Float accelerationZ;
    private Integer accuracy;
    private Long timestamp;

    private AccelerometerEvent(){ super(); }
    public static AccelerometerEvent build(){
        return new AccelerometerEvent();
    }

    public Float getAccelerationX() {
        return accelerationX;
    }

    public AccelerometerEvent setAccelerationX(Float accelerationX) {
        this.accelerationX = accelerationX;
        return this;
    }

    public Float getAccelerationY() {
        return accelerationY;
    }

    public AccelerometerEvent setAccelerationY(Float accelerationY) {
        this.accelerationY = accelerationY;
        return this;
    }

    public Float getAccelerationZ() {
        return accelerationZ;
    }

    public AccelerometerEvent setAccelerationZ(Float accelerationZ) {
        this.accelerationZ = accelerationZ;
        return this;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public AccelerometerEvent setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public AccelerometerEvent setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
