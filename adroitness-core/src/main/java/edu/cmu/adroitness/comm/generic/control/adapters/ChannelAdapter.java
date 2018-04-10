package edu.cmu.adroitness.comm.generic.control.adapters;

import android.content.Context;

import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;

/**
 * Created by oscarr on 3/15/16.
 *
 * Use a Channel Adapter that can access the application's API or data and publish messages on a
 * channel based on this data, and that likewise can receive messages and invoke functionality
 * inside the application. The adapter acts as a messaging client to the messaging system and
 * invokes applications functions via an application-supplied interface. This way, any application
 * can connect to the messaging system and be integrated with other applications as long as it has a
 * proper Channel Adapter.
 * http://www.enterpriseintegrationpatterns.com/patterns/messaging/ChannelAdapter.html
 */
public class ChannelAdapter {
    protected ResourceLocator mResourceLocator;
    protected MessageBroker mMB;
    protected Context mContext;

    public ChannelAdapter(){
        mResourceLocator = ResourceLocator.getExistingInstance();
        mMB = MessageBroker.getExistingInstance( this );
        mContext = mResourceLocator.getContext();
    }
}
