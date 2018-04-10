package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.activity.control.ActivityRecognitionService;

/**
 * Created by oscarr on 3/15/16.
 */
public final class ActivRecogAdapter extends ChannelAdapter {
    private static ActivRecogAdapter instance;

    private ActivRecogAdapter() {
        super();
    }

    public static ActivRecogAdapter getInstance() {
        if (instance == null) {
            instance = new ActivRecogAdapter();
        }
        return instance;
    }

    public void getARName(final MBRequest request){
        mResourceLocator.addRequest( ActivityRecognitionService.class, "getActivityName",
                request.get(Constants.AR_ACTIVITY_TYPE));
    }
}
