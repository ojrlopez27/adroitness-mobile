package edu.cmu.adroitness.client.sensors.accelerometer.model;

import edu.cmu.adroitness.comm.accelerometer.model.AccelerometerEvent;
import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by oscarr on 3/25/16.
 */
public class AccelerometerVO extends DataObject {
    private Float accelerationX;
    private Float accelerationY;
    private Float accelerationZ;
    private Integer accuracy;
    private Long timestamp;

    public AccelerometerVO(AccelerometerEvent event) {
        this.accelerationX = event.getAccelerationX();
        this.accelerationY = event.getAccelerationY();
        this.accelerationZ = event.getAccelerationZ();
        this.accuracy = event.getAccuracy();
        this.timestamp = event.getTimestamp();
    }

    public Float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(Float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public Float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(Float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public Float getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(Float accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getUUID(){
        if( id == null ){
            id = Math.round( accelerationX ) + "-" + Math.round( accelerationY )
                    + "-" + Math.round( accelerationY ) + "-" +  Math.round( accuracy );
        }
        return id;
    }
}
