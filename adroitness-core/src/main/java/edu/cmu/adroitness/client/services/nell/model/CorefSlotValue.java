package edu.cmu.adroitness.client.services.nell.model;

import java.util.List;

/**
 * Created by oscarr on 9/27/16.
 */

public class CorefSlotValue implements MicroReaderSlotValue {
    private int id;
    private List<CorefValue> spans;
    private CorefValue rep;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CorefValue> getSpans() {
        return spans;
    }

    public void setSpans(List<CorefValue> spans) {
        this.spans = spans;
    }

    public CorefValue getRep() {
        return rep;
    }

    public void setRep(CorefValue rep) {
        this.rep = rep;
    }
}
