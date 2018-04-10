package edu.cmu.adroitness.client.services.nell.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

/**
 * Created by oscarr on 3/2/16.
 */
public class NELLEntity extends DataObject implements MicroReaderSlotValue {
    private String entity;
    private String category;
    private double confidence;

    public NELLEntity(String[] attributes) {
        super();
        entity = attributes[0].trim();
        category = attributes[1].trim();
        confidence = Double.valueOf(attributes[2].trim());
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
