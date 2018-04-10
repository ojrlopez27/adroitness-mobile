package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.sensors.accelerometer.control.AccelerometerSensor;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;

/**
 * Created by oscarr on 3/15/16.
 */
public class AwareAdapter extends ChannelAdapter {
    private static AwareAdapter instance;

    private AwareAdapter() {
        super();
    }

    public static AwareAdapter getInstance() {
        if (instance == null) {
            instance = new AwareAdapter();
        }
        return instance;
    }

    public void stopSensors(MBRequest request) {
        AwareServiceWrapper.stopSensors();
    }

    public void stopPlugins(MBRequest request) {
        AwareServiceWrapper.stopPlugins();
    }

    public String getAwareSetting(MBRequest request) {
        return AwareServiceWrapper.getSetting(mContext,
                (String) request.get(Constants.SERVICE_SETTINGS));
    }

    public void startAwarePlugin(MBRequest request){
        AwareServiceWrapper.startPlugin(mContext, request);
    }

    public void stopAwarePlugin(MBRequest request){
        AwareServiceWrapper.stopPlugin(mContext, request);
    }

    public void changeSensorSettings(MBRequest mbRequest) {
        String sensorName = (String) mbRequest.get( Constants.SENSOR_NAME );
        if( sensorName != null ){
            Object sensorSetting = mbRequest.get( Constants.SENSOR_SETTING );
            if( sensorSetting.equals( Constants.SENSOR_STOP ) ){
                mResourceLocator.stopSensor(sensorName);
                return;
            }else if( sensorSetting.equals( Constants.SENSOR_START ) ){
                mResourceLocator.startSensor(sensorName);
            }
            if( sensorName.equals( Constants.SENSOR_ACCELEROMETER )){
                Long frequency = (Long) mbRequest.get( Constants.ACCELEROMETER_FREQUENCY );
                if( frequency != null ){
                    mResourceLocator.lookupSensor(AccelerometerSensor.class).setFrequency( frequency );
                }
            }else if( sensorName.equals( Constants.SENSOR_SMS )){
                //TODO
            }
        }
    }
}
