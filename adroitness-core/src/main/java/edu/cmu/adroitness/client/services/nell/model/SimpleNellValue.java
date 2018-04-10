package edu.cmu.adroitness.client.services.nell.model;

/**
 * Created by oscarr on 9/27/16.
 */
public class SimpleNellValue implements MicroReaderSlotValue {
    private String value;

    public SimpleNellValue(String jsonString) {
        this.value = jsonString.replace("\"", "");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
