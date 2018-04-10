package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.nell.control.NELLService;

/**
 * Created by oscarr on 8/29/16.
 */
public class NellAdapter extends ChannelAdapter {
    private static NellAdapter instance;

    private NellAdapter() {
        super();
    }

    public static NellAdapter getInstance() {
        if (instance == null) {
            instance = new NellAdapter();
        }
        return instance;
    }

    public void executeMacroReading( MBRequest mbRequest){
        mResourceLocator.addRequest( NELLService.class, "executeMacroReading",
                        (String) mbRequest.get(Constants.NELL_ENTITY) );
    }

    public void executeMicroReading( MBRequest mbRequest){
        mResourceLocator.addRequest( NELLService.class, "executeMicroReading",
                        (String) mbRequest.get(Constants.NELL_DOCUMENT) );
    }
}
