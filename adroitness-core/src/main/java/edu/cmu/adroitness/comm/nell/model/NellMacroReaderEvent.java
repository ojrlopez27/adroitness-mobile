package edu.cmu.adroitness.comm.nell.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.nell.model.MacroReaderResultVO;

/**
 * Created by oscarr on 8/29/16.
 */
public class NellMacroReaderEvent extends BaseEvent {
    private MacroReaderResultVO resultVO;

    private NellMacroReaderEvent(){ super(); }
    private NellMacroReaderEvent(int mbRequestId){ super( mbRequestId); }

    public static NellMacroReaderEvent build(){
        return new NellMacroReaderEvent();
    }
    public static NellMacroReaderEvent build(int mbRequestId){
        return new NellMacroReaderEvent(mbRequestId);
    }

    public MacroReaderResultVO getResultVO() {
        return resultVO;
    }

    public NellMacroReaderEvent setResultVO(MacroReaderResultVO resultVO) {
        this.resultVO = resultVO;
        return this;
    }
}
