package edu.cmu.adroitness.client.sensors.phonecall.model;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;

import java.util.ArrayList;

/**
 * Created by oscarr on 9/29/15.
 */
public class PhoneCallProposition extends PropositionalStatement {

    public PhoneCallProposition(){
        componentName = Constants.PHONECALL;
    }

    public PhoneCallProposition(String attribute, String operator, Object value) {
        super(attribute, operator, value.toString());
        componentName = Constants.PHONECALL;
    }

    public PhoneCallProposition(String attribute, String operator, Object value, String refAttribute) {
        super(attribute, operator, value.toString(), refAttribute);
        componentName = Constants.PHONECALL;
    }

    @Override
    public Object validate(Object objValue) {
        return false;
    }

    @Override
    public ArrayList validate() {
        return getList(null);
    }

    @Override
    public void postCreate() {
        //TODO
    }
}
