package edu.cmu.adroitness.client.services.nell.model;

/**
 * Created by oscarr on 9/27/16.
 */

public class DepParseValue {
    private String label;
    private String word1;
    private String word2;

    public DepParseValue(String label, String word1, String word2) {
        this.label = label;
        this.word1 = word1;
        this.word2 = word2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }
}
