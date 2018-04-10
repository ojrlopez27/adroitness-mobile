package edu.cmu.adroitness.comm.nell.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.nell.model.MicroReaderResultVO;

/**
 * Created by oscarr on 8/29/16.
 */
public class NellMicroReaderEvent extends BaseEvent {
    private MicroReaderResultVO resultVO;

    private NellMicroReaderEvent(){ super(); }
    private NellMicroReaderEvent(int mbRequestId){ super( mbRequestId); }

    public static NellMicroReaderEvent build(){
        return new NellMicroReaderEvent();
    }
    public static NellMicroReaderEvent build(int mbRequestId){
        return new NellMicroReaderEvent(mbRequestId);
    }

    public MicroReaderResultVO getResultVO() {
        return resultVO;
    }

    public NellMicroReaderEvent setResultVO(MicroReaderResultVO resultVO) {
        this.resultVO = resultVO;
        return this;
    }
}
