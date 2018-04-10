package edu.cmu.adroitness.comm.generic.control.adapters;

import com.aware.Aware_Preferences;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;

/**
 * Created by oscarr on 3/15/16.
 */
public final class BatteryAdapter extends ChannelAdapter {
    private static BatteryAdapter instance;

    private BatteryAdapter() {
        super();
    }

    public static BatteryAdapter getInstance() {
        if (instance == null) {
            instance = new BatteryAdapter();
        }
        return instance;
    }

    public boolean getStatusBattery(MBRequest mbRequest){
        return AwareServiceWrapper.getSetting(mContext,
                Aware_Preferences.STATUS_BATTERY).equals("true")? true : false;
    }
}
