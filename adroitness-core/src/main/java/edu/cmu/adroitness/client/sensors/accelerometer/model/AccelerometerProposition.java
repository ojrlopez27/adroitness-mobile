package edu.cmu.adroitness.client.sensors.accelerometer.model;

import edu.cmu.adroitness.comm.accelerometer.model.AccelerometerEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;
import edu.cmu.adroitness.client.sensors.accelerometer.control.AccelerometerSensor;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 10/14/15.
 */
public class AccelerometerProposition extends PropositionalStatement {

    /**
     * We need this constructor for json deserialization
     */
    public AccelerometerProposition(){
        super();
        componentName = Constants.ACCELEROMETER;
        subscribe();
    }

    public AccelerometerProposition(String attribute, String operator, String value){
        super( attribute, operator, value );
        componentName = Constants.ACCELEROMETER;
        subscribe();
    }

    @Override
    public void subscribe(){
        super.subscribe();
        startAccelerometer();
    }

    @Override
    public void unsubscribe(){
        stopAccelerometer();
        super.unsubscribe();
    }

    @Override
    public ArrayList validate() {
        return getList(ResourceLocator.getExistingInstance().lookupSensor(AccelerometerSensor.class)
                .getAccEvent());
    }


    private void startAccelerometer(){
        ResourceLocator.getExistingInstance().lookupSensor(AccelerometerSensor.class).startListening();
    }

    private void stopAccelerometer(){
        ResourceLocator.getExistingInstance().lookupSensor(AccelerometerSensor.class).stopListening();
    }

    @Override
    public Object validate(Object objValue) {
        try {
            AccelerometerVO vo = objValue instanceof AccelerometerEvent?
                    new AccelerometerVO( (AccelerometerEvent)objValue ) : (AccelerometerVO) objValue;
            Float valueDouble = 0.0f, attributeDouble = 0.0f;
            if (attribute.equals(Constants.ACCELEROMETER_X_AXIS)) {
                attributeDouble = vo.getAccelerationX();
            } else if (attribute.equals(Constants.ACCELEROMETER_Y_AXIS)) {
                attributeDouble = vo.getAccelerationY();
            } else if (attribute.equals(Constants.ACCELEROMETER_Z_AXIS)) {
                attributeDouble = vo.getAccelerationZ();
            } else if (attribute.equals(Constants.ACCELEROMETER_VECTOR_SUM)) {
                attributeDouble = getVectorSum(vo);
            } else if (attribute.equals(Constants.ACCELEROMETER_FREE_FALL)) {
                attributeDouble = getVectorSum(vo);
                value = "2"; // if vector sum is close to 0 2m/s (free fall)
                operator = Constants.OPERATOR_LOWER_THAN;
            } else if (attribute.equals(Constants.ACCELEROMETER_ACCURACY)) {
                attributeDouble = new Float( vo.getAccuracy() );
            }
            System.out.println("X: " + vo.getAccelerationX() + " Y: " + vo.getAccelerationY() + " Z: " +vo.getAccelerationZ() + "Sum: " + getVectorSum(vo));
            valueDouble = Float.valueOf(value);
            if( validateNumbers(attributeDouble, valueDouble) ){
                return vo;
            }
        }catch(Exception e){
            //do nothing
        }
        return null;
    }

    @Override
    public void postCreate() {
        //TODO
    }


    private Float getVectorSum(AccelerometerVO event){
        if( event.getAccelerationX() != null && event.getAccelerationY() != null
                && event.getAccelerationZ() != null ) {
            return new Float( Math.sqrt(Math.pow(event.getAccelerationX(), 2) + Math.pow(
                    event.getAccelerationY(), 2) + Math.pow(event.getAccelerationZ(), 2)) );
        }
        return null;
    }


    public void onEvent(AccelerometerEvent event){
        List<DataObject> elements = new ArrayList<>();
        elements.add( new AccelerometerVO( event ).setLinkedToObjectID( false ) );
        super.onEvent( elements, null );


//        if( rule != null ){
//            Object result = validate( event );
//            if( result != null ){
//                System.out.println("RETURNING TRUE 3");
//            }
//            rule.setPropositionFlag( this, result != null, null );
//        }
    }
}
