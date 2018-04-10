package edu.cmu.adroitness.client.services.nell.model;

/**
 * Created by oscarr on 9/27/16.
 */

public class CorefValue {
    /**
     * start position
     */
    private int s;
    /**
     * end position
     */
    private int e;
    private int sI;

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getsI() {
        return sI;
    }

    public void setsI(int sI) {
        this.sI = sI;
    }
}
