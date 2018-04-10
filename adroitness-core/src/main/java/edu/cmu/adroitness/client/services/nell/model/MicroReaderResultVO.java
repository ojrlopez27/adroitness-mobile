package edu.cmu.adroitness.client.services.nell.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.List;

/**
 * Created by oscarr on 8/29/16.
 */
public class MicroReaderResultVO extends DataObject {
    private List<MicroReaderElement> results;

    public List<MicroReaderElement> getResults() {
        return results;
    }

    public void setResults(List<MicroReaderElement> results) {
        this.results = results;
    }
}
