package edu.cmu.adroitness.client.services.nell.model;

/**
 * Created by oscarr on 9/27/16.
 */

public class TokenSlotValue implements MicroReaderSlotValue {
    /**
     * Word
     */
    private String str;
    /**
     * Start position (at document?)
     */
    private int s;
    /**
     * End position (at document?)
     */
    private int e;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

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
}
