package edu.cmu.adroitness.comm.generic.control;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.greenrobot.eventbus.EventBus;
import java.util.Collection;

/**
 * Created by oscarr on 4/10/18.
 */

public class AdroitEventBus{
    private EventBus eventBus;

    //FIXME: ojrl: subscription exceptions for subscriber (Object) and event (Class)
    private Multimap<Object, Class> subscriptionExceptions;
    static AdroitEventBus instance;

    public AdroitEventBus() {
        eventBus = EventBus.getDefault();
        subscriptionExceptions = ArrayListMultimap.create();
    }

    public static AdroitEventBus getDefault() {
        if( instance == null ){
            instance = new AdroitEventBus();
        }
        return instance;
    }


    //FIXME: ojrl:
    /**
     * This method checks whether there are exceptions for this subscriber that avoids it receiving
     * updates for this particular event
     * @param subscriber
     * @param event
     * @return
     */
    private boolean checkExceptions( Object subscriber, Object event ){
        Collection<Class> events = subscriptionExceptions.get( subscriber );
        return events.contains( event.getClass() );
    }


    //FIXME: ojrl
    public void addSubscriptionException( Object subscriber, Class event ){
        subscriptionExceptions.put( subscriber, event );
    }

    //FIXME: ojrl
    public void removeSubscriptionException( Object subscriber, Class event ){
        subscriptionExceptions.remove( subscriber, event );
    }

    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    public boolean isRegistered(Object subscriber) {
        return eventBus.isRegistered(subscriber);
    }

    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    public void postSticky(Object event) {
        eventBus.postSticky(event);
    }

    public void post(Object event) {
        eventBus.post(event);
    }

    public <T> T getStickyEvent(Class<T> eventType) {
        return eventBus.getStickyEvent(eventType);
    }
}
