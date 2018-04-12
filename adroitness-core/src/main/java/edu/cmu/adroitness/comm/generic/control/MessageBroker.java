package edu.cmu.adroitness.comm.generic.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import edu.cmu.adroitness.comm.generic.control.adapters.ActivRecogAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.AlarmAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.AudioAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.AwareAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.BatteryAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.CalendarAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.ChannelAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.DecisionRuleAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.GmailAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.GooglePlayAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.GoogleSpeechRecognitionAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.HotelAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.ImapAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.LocationAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.NellAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.Red5StreamingAdapter;
import edu.cmu.adroitness.comm.generic.control.adapters.SmsAdapter;

import edu.cmu.adroitness.comm.generic.control.adapters.WeatherAdapter;
import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.MiddlewareException;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.commons.view.AccountPickerActivity;


/**
 * @author oscarr
 *
 * Message broker is an intermediary program module which translates a message from the formal
 * messaging protocol of the sender to the formal messaging protocol of the receiver. Message brokers
 * are elements in telecommunication networks where programs (software applications) communicate by
 * exchanging formally-defined messages. Message brokers are a building block of Message oriented
 * middleware (MOM). A message broker is an architectural pattern for message validation, message
 * transformation and message routing.[1] It mediates communication amongst applications, minimizing
 * the mutual awareness that applications should have of each other in order to be able to exchange
 * messages, effectively implementing decoupling.
 * The purpose of a broker is to take incoming messages from applications and perform some action on
 * them. The following are examples of actions that might be taken in by the broker:
 * - Route messages to one or more of many destinations
 * - Transform messages to an alternative representation
 * - Perform message aggregation, decomposing messages into multiple messages and sending them to
 * their destination, then recomposing the responses into one message to return to the user
 * - Interact with an external repository to augment a message or store it
 * - Invoke Web services to retrieve data
 * - Respond to events or errors
 * - Provide content and topic-based message routing using the publish–subscribe pattern
 *
 */
public final class MessageBroker {
    /** Publish/subscribe */
    private static AdroitEventBus mEventBus;
    private HashMap<Integer, SubscriberEvent> mSubscribers;

    private static MessageBroker instance;
    private static Context mContext;
    private static ResourceLocator mResourceLocator;
    private static LruCache<String, MethodChannelAdapter> methodMap;
    private static ArrayList<String> requiredServices;

    /** Cache Memory **/
    private static LruCache<Object, Object> mCache;

    /** Requests for onServiceConnected **/
    private static ArrayList<MBRequest> mRequests = new ArrayList<>();


    /**********************************************************************************************/
    /******************************                           *************************************/
    /****************************** MessageBroker's LIFECYCLE *************************************/
    /******************************                           *************************************/
    /**********************************************************************************************/

    private MessageBroker( Context app ) {
        if( !(app instanceof Activity) ){
            /*throw new MiddlewareException("Middleware must be called from an activity when created " +
                    "the first time", app);*/
        }
        mContext = app.getApplicationContext();
        mEventBus = AdroitEventBus.getDefault();
        mCache = new LruCache<>(500);
        methodMap = new LruCache<>(500);
        mSubscribers = new HashMap<>();
        subscribe(this);
    }


    @Nullable
    public static MessageBroker getExistingInstance(Object caller) {
        mResourceLocator.addActivity(caller);
        return instance;
    }

    /**
     * Singleton
     * @return
     */
    public static MessageBroker getInstance(Context app) {
        if (instance == null) {
            if (app == null) {
                throw new MiddlewareException("There is no Context to bind the Message Broker.");
            } else {
                instance = new MessageBroker( app );
                postCreate( app );
                instance.subscribe( instance);
            }
        }
        if( mResourceLocator != null ) {
            mResourceLocator.addActivity(app);
        }
        return instance;
    }


    public static MessageBroker getInstance(Context app, ArrayList<String> services) {
        requiredServices = services;
        getInstance( app );
        return instance;
    }

    /**
     * We need to execute some functionality outside the MB constructor otherwise
     * we would enter into an endless loop and circular references (e.g., ResourceLocator
     * constructor calls the MB getInstance which in turns call the constructor which
     * in turns calls the ResourceLocator...)
     */
    private static void postCreate( Context app ) {
        mResourceLocator = ResourceLocator.getInstance(mContext);
        mResourceLocator.addServices( requiredServices );
        mResourceLocator.bindServices();
        mResourceLocator.addSensors();
        mResourceLocator.addEffectors();
        mResourceLocator.addActivity( app );
        initMap();
    }

    /**
     * This method asks user to select a google account (for calendar, email, contacts, etc.)
     */
    private void chooseAccount() {
        if( mResourceLocator.getAccount(Constants.GOOGLE_ACCOUNT) == null ) {
            send(this, MBRequest.build(Constants.MSG_LAUNCH_ACTIVITY)
                    .put(Constants.BUNDLE_ACTIVITY_NAME, AccountPickerActivity.class.getCanonicalName()));
        }
    }


    public void destroy() {
        mResourceLocator.destroy();
        mResourceLocator = null;
        instance = null;
        mContext = null;
        mCache.evictAll();
        mSubscribers = null;
        Util.release();
        System.gc();
    }

    private static void initMap(){
        for(Object keyObj : mResourceLocator.getMappingsProperties().keySet() ){
            String key = (String) keyObj;
            String[] value = mResourceLocator.getMappingsProperty(key).split(":");
            ChannelAdapter adapter = getAdapter( value[1] );
            methodMap.put( key, new MethodChannelAdapter(value[0], adapter ) );
        }
    }

    public static ChannelAdapter getAdapter(String name){
        if( name.equals(Constants.AUDIO) ) return AudioAdapter.getInstance();
        if( name.equals(Constants.CALENDAR) ) return CalendarAdapter.getInstance();
        if( name.equals(Constants.WEATHER) ) return WeatherAdapter.getInstance();
        if( name.equals(Constants.LOCATION) ) return LocationAdapter.getInstance();
        if( name.equals(Constants.HOTEL) ) return HotelAdapter.getInstance();
        if( name.equals(Constants.AWARE) ) return AwareAdapter.getInstance();
        if( name.equals(Constants.DECISION_RULE) ) return DecisionRuleAdapter.getInstance();
        if( name.equals(Constants.GOOGLE_PLAY) ) return GooglePlayAdapter.getInstance();
        if( name.equals(Constants.ALARM) ) return AlarmAdapter.getInstance();
        if( name.equals(Constants.BATTERY) ) return BatteryAdapter.getInstance();
        if( name.equals(Constants.ACTIVITY_RECOGNITION) ) return ActivRecogAdapter.getInstance();
        if( name.equals(Constants.SMS) ) return SmsAdapter.getInstance();
        if( name.equals(Constants.RED5STREAMING) ) return Red5StreamingAdapter.getInstance();
        if( name.equals(Constants.GMAILMANAGER)) return GmailAdapter.getInstance();
        if( name.equals(Constants.IMAP_MANAGER)) return ImapAdapter.getInstance();
        if( name.equals(Constants.NELL )) return NellAdapter.getInstance();
        if(name.equals(Constants.GOOGLESPEECHRECOGNIZER)) return GoogleSpeechRecognitionAdapter.getInstance();

        //your channel adapter goes here:

        return null;
    }


    /**********************************************************************************************/
    /**************************************                ****************************************/
    /************************************** HELPER CLASSES ****************************************/
    /**************************************                ****************************************/
    /**********************************************************************************************/


    class SubscriberEvent {
        private Class event;
        private Object subscriber;

        public SubscriberEvent(Class event, Object subscriber) {
            this.event = event;
            this.subscriber = subscriber;
        }

        public Class getEvent() {
            return event;
        }

        public void setEvent(Class event) {
            this.event = event;
        }

        public Object getSubscriber() {
            return subscriber;
        }

        public void setSubscriber(Object subscriber) {
            this.subscriber = subscriber;
        }
    }


    static class MethodChannelAdapter{
        private String methodName;
        private ChannelAdapter adapter;

        public MethodChannelAdapter(String methodName, ChannelAdapter channelAdapter) {
            this.methodName = methodName;
            this.adapter = channelAdapter;
        }

        public String getMethodName() {
            return methodName;
        }

        public ChannelAdapter getAdapter() {
            return adapter;
        }

        public void setAdapter(ChannelAdapter adapter) {
            this.adapter = adapter;
        }
    }


    /**********************************************************************************************/
    /*******************************                            ***********************************/
    /******************************* REQUEST AND EVENT HANDLERS ***********************************/
    /*******************************                            ***********************************/
    /**********************************************************************************************/
    @Subscribe
    public void onEvent(MiddlewareNotificationEvent event){
        if( !event.getSourceName().equals( AccountPickerActivity.class.getName() ) ){
            chooseAccount();
        }
    }

    /**
     * Use this method to send asynchronous requests to the backend (services, background tasks, etc.)
     * If your request returns a result, then it should be handled by an event handler defined outside
     * the Message Broker (usually in the caller class that invoked the message broker's send method)
     * The result of this request is delivered to all the subscribers of the resulting event
     *
     * @param caller this is used as a reference for callbacks, monitoring and logging purposes.
     * @param request
     */
    public void send(final Object caller, final Object request) {
        Log.d("MessageBroker", "1. send: " + request);
        Log.d("MessageBroker", "2. send");
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("MessageBroker", "3. send");
                if (request instanceof MBRequest) {
                    Log.d("MessageBroker", "4. send");
                    executeRequest((MBRequest) request);
                    Log.d("MessageBroker", "5. send");
                } else {
                    Log.d("MessageBroker", "6. send");
                    post(request);
                    Log.d("MessageBroker", "7. send");
                }
            }
        });
    }

    /**
     * This is an asynchronous request like {@link #send(Object, Object)} but the difference is that
     * this method requires the response event to be delivered to only the caller subscriber (P2P),
     * not to all the objects that are subscribed to this event (Broadcasting).
     *
     * @param subscriber This is the subscriber object that will receive the response
     * @param request    This is the request message
     * @param event      this is the event that the subscriber will handle on its onEvent(event) method
     */
    public void sendAndReceive(Object subscriber, Class event, Object request) {
        // pass the caller to the get and monitoring module
        SubscriberEvent subsEvent = new SubscriberEvent(event, subscriber);
        mSubscribers.put(request.hashCode(), subsEvent);
        send(this, request);
    }


    /**
     * This method checks whether the event has a valid request id. If so, it looks up the
     * corresponding subscriber for this event and set it into the event object.
     *
     * @param event
     * @return it returns the corresponding subscriber
     */
    private Object linkSubscriber(Object event) {
        Object subscriber = null;
        if (event instanceof BaseEvent) {
            SubscriberEvent subsEvent = mSubscribers.remove( ((BaseEvent)event).getMbRequestId() );
            if (subsEvent != null && subsEvent.getEvent() == event.getClass()) {
                subscriber = subsEvent.getSubscriber();
                ((BaseEvent) event).setSubscriber(subscriber);
            }
        }
        return subscriber;
    }


    /**
     * Use this method to send a synchronous request and get an immediate result. The request should
     * not take too much processing time, so it warranties not to block the main thread (UI thread).
     *
     * @param caller this is used as a reference for callbacks, monitoring and logging purposes.
     * @param request
     * @return
     */
    public Object get(Object caller, final MBRequest request) {
        // pass the caller to the privacy and monitoring module
        return Util.executeSync(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return executeRequest(request);
            }
        });
    }

    public static void set(MBRequest mbRequest) {
        instance.executeRequest( mbRequest );
    }

    /**
     * This method uses reflection in order to execute MB's send requests.
     * @param request
     */
    private Object executeRequest(MBRequest request){
        try {
            MethodChannelAdapter mca = methodMap.get( request.getRequestId() );
            String stringMethod = mca.getMethodName();
            ChannelAdapter adapter = mca.getAdapter();
            return Util.executeMethod( adapter, stringMethod, false, request  );
        }catch (Exception e){
            if( e instanceof NoSuchElementException ) {
                mResourceLocator.getTopActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "The MBRequest object has a non valid id",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Use this method to subscribe to messages that are published by other components. Using this
     * method will allow you implement overloaded versions of onEvent method (a event handler method)
     *
     * @param subscriber
     */
    public void subscribe(Object subscriber) {
        if ( subscriber.getClass().isAnnotationPresent(Subscribe.class) && !mEventBus.isRegistered(subscriber)) {
            mEventBus.register(subscriber);
        }
        mResourceLocator.addActivity(subscriber);
    }


    /**
     * This method subscribes the subscriber object to a specific event updates. However, unless
     * {@link #subscribe(Object)}, this method set the update process to a hold-on state until
     * explicit notification from the subscriber of being ready to start receiving updates is sent.
     *
     * @param subscriber
     * @param event
     */
    public void subscribe(Object subscriber, Class... event) {
        //add a subscription exception to avoid receiving updates until the subscriber is ready
        for (Class e : event) {
            mEventBus.addSubscriptionException(subscriber, e);
        }
        subscribe(subscriber);
    }


    /**
     * Use this method to unsubscribe to any kind of messages published by other components. This is
     * useful to improve the resource management, for instance, use this method when the activity
     * is paused (onPause method) and subscribe again when it resumes (onResume method)
     *
     * @param subscriber
     */
    public void unsubscribe(Object subscriber) {
        mEventBus.unregister(subscriber);
    }


    public void unsubscribe(Object subscriber, Class event) {
        mEventBus.addSubscriptionException(subscriber, event);
    }

    public void removeSubscriptionException(Object subscriber, Class event) {
        mEventBus.removeSubscriptionException(subscriber, event);
    }


    /**
     * This method posts a sticky event, that is, the event is cached by the message broker so you
     * can get it anytime by using getSticky method. This is for internal use of the middleware.
     *
     * @param event
     */
    public void postSticky(Object event) {
        linkSubscriber( event );
        mEventBus.postSticky(event);
    }

    private void post(Object event) {
        linkSubscriber( event );
        mEventBus.post(event);
    }

    /**
     * This method gets a specific event which has been cached by the message broker. This is for
     * internal use of the middleware.
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        return mEventBus.getStickyEvent(eventType);
    }



    /****** COMMON ******************************************************************************/

    public static void setContext(Context context) {
        MessageBroker.mContext = context;
    }


    public void processRequests() {
        synchronized (mRequests) {
            for (MBRequest request : mRequests) {
                if (request.get(Constants.BUNDLE_MESSAGE_TYPE) != null
                        && (request.get(Constants.BUNDLE_MESSAGE_TYPE)).equals("NEWS")) {
                    MessageBroker.getInstance(mContext).send(this, request);
                    mRequests.remove(request);
                }
            }
        }
    }

    public List<MBRequest> getRequests(){
        return mRequests;
    }


    /**********************************************************************************************/
    /**********************************              **********************************************/
    /********************************** CACHE MEMORY **********************************************/
    /**********************************              **********************************************/
    /**********************************************************************************************/

    /**
     * This method adds an object to the mCache memory
     * @param object object to be added
     * @param id this is used to retrieve the object
     */
    public void addObjToCache(Object id, Object object) {
        synchronized (mCache) {
            mCache.put(id, object);
        }
    }

    /**
     * This method retrieves an object from the mCache memory
     * @param id of the object
     * @param remove whether the object must be removed from the mCache or not
     * @return
     */
    public Object getObjFromCache( Object id , boolean remove ){
        synchronized (mCache) {
            if (remove) {
                return mCache.remove(id);
            }
            return mCache.get(id);
        }
    }
}
