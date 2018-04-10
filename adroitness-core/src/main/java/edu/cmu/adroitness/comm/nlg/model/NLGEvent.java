package edu.cmu.adroitness.comm.nlg.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;

/**
 * Created by oscarr on 4/21/17.
 */

public class NLGEvent extends BaseEvent {
    private String output;

    private NLGEvent(){ super(); }
    private NLGEvent(int mbRequestId){ super( mbRequestId); }

    public static NLGEvent build(){
        return new NLGEvent();
    }
    public static NLGEvent build(int mbRequestId){
        return new NLGEvent(mbRequestId);
    }

    public String getOutput() {
        return output;
    }

    public NLGEvent setOutput(String output) {
        this.output = output;
        return this;
    }
}
