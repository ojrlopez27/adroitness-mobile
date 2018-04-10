package edu.cmu.adroitness.comm.tests.control;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.comm.tests.model.PerformanceTestVO;

/**
 * Created by oscarr on 9/5/17.
 */

public class PerformanceTestEvent extends BaseEvent {
    private PerformanceTestVO vo;


    private PerformanceTestEvent(){ super(); }
    private PerformanceTestEvent(int mbRequestId){ super( mbRequestId); }

    public static PerformanceTestEvent build(){
        return new PerformanceTestEvent();
    }
    public static PerformanceTestEvent build(int mbRequestId){
        return new PerformanceTestEvent(mbRequestId);
    }

    public PerformanceTestVO getVo() {
        return vo;
    }

    public void setVo(PerformanceTestVO vo) {
        this.vo = vo;
    }
}
