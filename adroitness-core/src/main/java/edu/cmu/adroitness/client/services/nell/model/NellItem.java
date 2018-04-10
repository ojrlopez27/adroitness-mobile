package edu.cmu.adroitness.client.services.nell.model;

import java.util.List;

/**
 * Created by oscarr on 9/22/16.
 */

public class NellItem implements Comparable{
    private String ent1;
    private String ent2;
    private String predicate;
    private String lit1;
    private String lit2;
    private Double globalScore;
    private List<NellJustification> justifications;

    public String getEnt1() {
        return ent1;
    }

    public void setEnt1(String ent1) {
        this.ent1 = ent1;
    }

    public String getEnt2() {
        return ent2;
    }

    public void setEnt2(String ent2) {
        this.ent2 = ent2;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getLit1() {
        return lit1;
    }

    public void setLit1(String lit1) {
        this.lit1 = lit1;
    }

    public String getLit2() {
        return lit2;
    }

    public void setLit2(String lit2) {
        this.lit2 = lit2;
    }

    public List<NellJustification> getJustifications() {
        return justifications;
    }

    public void setJustifications(List<NellJustification> justifications) {
        this.justifications = justifications;
    }

    public Double getGlobalScore() {
        return globalScore;
    }

    public void setGlobalScore(Double globalScore) {
        this.globalScore = globalScore;
    }

    @Override
    public int compareTo(Object another) {
        return ((NellItem) another).getGlobalScore().compareTo(this.globalScore);
    }
}
