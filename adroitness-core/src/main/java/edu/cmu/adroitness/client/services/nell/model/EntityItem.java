package edu.cmu.adroitness.client.services.nell.model;

import java.util.List;

/**
 * Created by oscarr on 9/22/16.
 */
public class EntityItem {
    private String entity;
    private List<String> referredToByToken;
    private List<String> refersToConcept;
    private List<String> literalString;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public List<String> getReferredToByToken() {
        return referredToByToken;
    }

    public void setReferredToByToken(List<String> referredToByToken) {
        this.referredToByToken = referredToByToken;
    }

    public List<String> getRefersToConcept() {
        return refersToConcept;
    }

    public void setRefersToConcept(List<String> refersToConcept) {
        this.refersToConcept = refersToConcept;
    }

    public List<String> getLiteralString() {
        return literalString;
    }

    public void setLiteralString(List<String> literalString) {
        this.literalString = literalString;
    }
}
