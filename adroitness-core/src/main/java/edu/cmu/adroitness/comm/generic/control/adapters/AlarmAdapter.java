package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.effectors.alarm.control.AlarmEffector;

/**
 * Created by oscarr on 3/15/16.
 */
public class AlarmAdapter extends ChannelAdapter {
    private static AlarmAdapter instance;

    private AlarmAdapter() {
        super();
    }

    public static AlarmAdapter getInstance() {
        if (instance == null) {
            instance = new AlarmAdapter();
        }
        return instance;
    }

    public void playAlarm( MBRequest request){
            mResourceLocator.addRequest( AlarmEffector.class,
                    "playRingtone", ((Integer) request.get( Constants.ALARM_RINGTONE_TYPE )));
         }
}
