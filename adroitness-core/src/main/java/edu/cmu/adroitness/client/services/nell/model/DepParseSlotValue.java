package edu.cmu.adroitness.client.services.nell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 9/27/16.
 */
public class DepParseSlotValue implements MicroReaderSlotValue {
    private List<DepParseValue> values;

    public DepParseSlotValue(String jsonString) {
        String[] elements = jsonString.replace("\\n", "|").split("\\|");
        if( values == null ){
            values = new ArrayList<>();
        }
        for(String element : elements){
            element = element.replace("\"", "");
            if( !element.isEmpty() ) {
                String label = element.substring(0, element.indexOf("("));
                String word2 = element.substring(element.indexOf("(") + 1, element.indexOf(","));
                String word1 = element.substring(element.indexOf(",") + 1, element.length() - 1);
                values.add(new DepParseValue(label, word1, word2));
            }
        }
    }

    public List<DepParseValue> getValues() {
        return values;
    }

    public void setValues(List<DepParseValue> values) {
        this.values = values;
    }
}
