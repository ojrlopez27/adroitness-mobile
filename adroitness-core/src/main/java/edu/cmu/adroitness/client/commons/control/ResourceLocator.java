package edu.cmu.adroitness.client.commons.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MiddlewareNotificationEvent;
import edu.cmu.adroitness.commons.rules.control.DecisionRuleValidator;
import edu.cmu.adroitness.commons.rules.model.DecisionRule;
import edu.cmu.adroitness.commons.view.AccountPickerActivity;
import edu.cmu.adroitness.effectors.alarm.control.AlarmEffector;
import edu.cmu.adroitness.effectors.generic.control.EffectorDataReceiver;
import edu.cmu.adroitness.effectors.generic.control.EffectorObserver;
import edu.cmu.adroitness.client.effectors.sms.control.SmsEffector;
import edu.cmu.adroitness.client.sensors.accelerometer.control.AccelerometerSensor;
import edu.cmu.adroitness.client.sensors.generic.control.SensorDataReceiver;
import edu.cmu.adroitness.client.sensors.generic.control.SensorObserver;
import edu.cmu.adroitness.client.sensors.sms.control.SmsSensor;
import edu.cmu.adroitness.client.services.activity.control.ActivityRecognitionService;
import edu.cmu.adroitness.client.services.booking.control.HotelReservationService;
import edu.cmu.adroitness.client.services.calendar.control.CalendarService;
import edu.cmu.adroitness.client.services.email.control.GmailManagerService;
import edu.cmu.adroitness.client.services.email.control.IMAPManagerService;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;
import edu.cmu.adroitness.client.services.generic.control.ExternalAppCommService;
import edu.cmu.adroitness.client.services.generic.control.GenericService;
import edu.cmu.adroitness.client.services.generic.control.ServiceDataReceiver;
import edu.cmu.adroitness.client.services.googlespeechrecognition.control.GoogleSpeechRecognitionService;
import edu.cmu.adroitness.client.services.location.control.LocationService;
import edu.cmu.adroitness.client.services.nell.control.NELLService;
import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamingService;
import edu.cmu.adroitness.client.services.weather.control.WeatherService;

/**
 *
 * The service locator pattern is a design pattern used in software development to encapsulate the
 * processes involved in obtaining a service with a strong abstraction layer. This pattern uses a
 * central registry known as the "service locator", which on request returns the information
 * necessary to perform a certain task.
 * @see @link https://msdn.microsoft.com/en-us/library/ff648968.aspx
 *
 * Created by oscarr on 8/10/15.
 */
public final class ResourceLocator {

    private ConcurrentHashMap<Class, MiddServiceConnection> mServicesHash;
    private ConcurrentHashMap<Class, SensorObserver>  mSensorsHash;
    private ConcurrentHashMap<Class, EffectorObserver> mEffectorsHash;
    private ConcurrentHashMap<String, DecisionRule> mDecisionRulesHash;
    private Multimap<Class, LookupRequest> mRequests;
    private Map<Class, LookupRequest> requestsToRemove;
    private SensorDataReceiver sensorDataReceiver;
    private ServiceDataReceiver serviceDataReceiver;
    private EffectorDataReceiver effectorDataReceiver;
    private ArrayList<String> actionsServiceReceiver;
    private ArrayList<String> actionsSensorReceiver;
    private ArrayList<String> actionsEffectorReceiver;
    private ArrayList<TopActivityObserver> mTopActivitiesObservers;
    private static ResourceLocator instance;
    private static Context mContext;
    private static Properties configProp;
    private static Properties mappingsProp;
    private static ConcurrentHashMap<String, Class> mServicesCatalog;
    private static ConcurrentHashMap<String, MiddlewareAccessibilityServiceImpl> mAccessibilityServicesHash;
    private static boolean active = false;
    private static int numInitializedServices;
    private Lock lock;

    /** Tracking list of started activities **/
    private ArrayList<Class> mClassActivitiesList = new ArrayList<>();
    private ArrayList<Activity> mActivitiesList = new ArrayList<>();

    /** Accounts **/
    private static String googleAccount;

    private ResourceLocator(Context context) {
        active = true;
        mServicesHash = new ConcurrentHashMap<>();
        mAccessibilityServicesHash = new ConcurrentHashMap<>();
        mSensorsHash = new ConcurrentHashMap<>();
        mEffectorsHash = new ConcurrentHashMap<>();
        mDecisionRulesHash = new ConcurrentHashMap<>();
        mRequests = ArrayListMultimap.create();
        mContext = context;
        sensorDataReceiver = new SensorDataReceiver(mContext);
        serviceDataReceiver = new ServiceDataReceiver(mContext);
        effectorDataReceiver = new EffectorDataReceiver(mContext);
        actionsServiceReceiver = new ArrayList<>();
        actionsSensorReceiver = new ArrayList<>();
        actionsEffectorReceiver = new ArrayList<>();
        configProp = Util.loadConfigAssets( context, Constants.CONFIG_PROPERTIES);
        mappingsProp = Util.loadMappingsAssets( context, Constants.MAPPINGS_PROPERTIES);
        mTopActivitiesObservers = new ArrayList<>();
        lock = new ReentrantLock();
        requestsToRemove = new HashMap();
        initializeServices();
    }

    public static ResourceLocator getInstance(Context context) {
        if (instance == null) {
            instance = new ResourceLocator( context );
            DecisionRuleValidator.getInstance();
        }
        return instance;
    }

    @Nullable
    public static ResourceLocator getExistingInstance() {
        return instance;
    }


    /**
     * Add all the available Adroitness services here (those which extends GenericService)
     */
    public static void initializeServices(){
        mServicesCatalog = new ConcurrentHashMap<>();
        mServicesCatalog.put( Constants.ADD_SERVICE_AWARE, AwareServiceWrapper.class );
        mServicesCatalog.put( Constants.ADD_SERVICE_ACTIVITY_RECOGNITION, ActivityRecognitionService.class );
        mServicesCatalog.put( Constants.ADD_SERVICE_HOTEL_RESERVATION, HotelReservationService.class );
        mServicesCatalog.put( Constants.ADD_SERVICE_LOCATION, LocationService.class );
        mServicesCatalog.put( Constants.ADD_SERVICE_WEATHER, WeatherService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_CALENDAR, CalendarService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_EXTERNAL_COMMUNICATION, ExternalAppCommService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_RED5STREAMING, Red5StreamingService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_GMAIL_SERVICE, GmailManagerService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_IMAP_SERVICE, IMAPManagerService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_NELL, NELLService.class);
        mServicesCatalog.put( Constants.ADD_SERVICE_GOOGLE_SPEECH_RECOGNITION, GoogleSpeechRecognitionService.class);
        // add your service here:

    }

    public static void processAccessibilityServices(){
        if( numInitializedServices == (mServicesCatalog.values().size() - 1 ) ||
                numInitializedServices == (instance.mServicesHash.values().size() - 1 )){ // -1: AwareWrapperService
            if( mAccessibilityServicesHash == null || mAccessibilityServicesHash.isEmpty() ){
                MessageBroker.getInstance(mContext).send( instance, MiddlewareNotificationEvent.build() );
            }else {
                for (final MiddlewareAccessibilityServiceImpl service : mAccessibilityServicesHash.values()) {
                    if (!service.getServiceInterface().isRunning()) {
                        service.getServiceInterface().promptEnable();
                    }
                }
            }
        }
    }

    public void addAccessibilityService(MiddlewareAccessibilityService service){
        mAccessibilityServicesHash.put( service.getClass().getName(),
                new MiddlewareAccessibilityServiceImpl(service ));
    }

    public void addAccessibilityService(Class serviceInterface, AccessibilityService serviceImpl){
        MiddlewareAccessibilityServiceImpl impl = mAccessibilityServicesHash.remove(serviceInterface.getName());
        if( impl != null ) {
            impl.setServiceImpl( serviceImpl );
            mAccessibilityServicesHash.put(serviceInterface.getName(), impl);
        }
    }

    /**
     * This method has been replaced by lookupService
     * @param service
     * @param <T>
     * @return
     */
    @Nullable
    @Deprecated
    public <T extends GenericService> T getService(Class<T> service){
        return lookupService(service);
    }

    public void addRequest(final Class resource, final String methodName, final Object... params){
        GenericService service = lookupService( resource, true );
        LookupRequest request = new LookupRequest() {
            @Override
            public void onExecute() {
                Util.executeMethod( lookupService( resource ), methodName, false, params );
            }
        };
        if( service == null ) {
            mRequests.put(resource, request);
        }else{
            request.onExecute();
        }
    }

    public void pushRequest(final Object obj, final String methodName, final Object... params) {
        Log.d("ResourceLocator", "*** pushRequest 1. Method: " + methodName);
        final Class clazz = obj.getClass();
        lock.lock();
        mRequests.put(clazz, new LookupRequest() {
            @Override
            public void onExecute() {
                Log.d("ResourceLocator", "*** pushRequest 2 (inside onExecute for method: " + methodName + " - status start)");
                Util.executeMethod( obj, methodName, false, params);
                Log.d("ResourceLocator", "*** pushRequest 3 (after executeMethod for " + methodName +")");
            }
        });
        lock.unlock();
        if( mRequests.get(clazz).size() == 1 ){
            popRequest( clazz );
        }
    }

    public void popRequest(Class resource){
        lock.lock();
        mRequests.remove(resource, requestsToRemove.remove(resource) );
        ArrayList requests = new ArrayList( mRequests.get(resource) );
        LookupRequest request;
        lock.unlock();
        Log.d("ResourceLocator", "*** popRequest 1. Class: " + resource);
        if( requests != null && !requests.isEmpty() ){
            Log.d("ResourceLocator", "*** popRequest 2. (request list is not empty and before onExecute)");
            request = (LookupRequest) requests.get(0);
            requestsToRemove.put(resource, request);
            request.onExecute();
            Log.d("ResourceLocator", "*** popRequest 3 (after onExecute)");
        }
    }

    public Multimap<Class, LookupRequest> getRequests(){
        return mRequests;
    }

    public void processRequests(Class resource){
        for(LookupRequest lookupRequest : getRequests().removeAll( resource ) ){
            lookupRequest.onExecute();
        }
    }

    /**
     * Looks up a specific service. If the call is invoked from addRequest (internalCall = true),
     * then it just return the getService (it could return a null reference) otherwise it will return
     * the service or a MiddlewareException if null.
     * @param service
     * @param internalCall
     * @param <T>
     * @return
     */
    public <T extends GenericService> T lookupService( Class<T> service, boolean internalCall ){
        MiddServiceConnection connection;
        if( mServicesHash != null && (connection = mServicesHash.get(service)) != null) {
            if( internalCall ){
                return (T)connection.getService();
            }else {
                if (connection.getService() == null) {
                    throw new MiddlewareException("Service " + service.getName() + " not initialized.");
                }
                return (T) connection.getService();
            }
        }
        addService(service);
        return null;
    }

    /**
     * Same as lookupService(class, boolean). Use this method only when your looking up a service
     * from another service.
     * @param service
     * @param <T>
     * @return
     */
    public <T extends GenericService> T lookupService( Class<T> service ){
        return lookupService( service, false );
    }

    @Nullable
    public  <T extends SensorObserver> T lookupSensor(Class<T> sensor){
        if( mSensorsHash != null ) {
            return (T) mSensorsHash.get(sensor);
        }
        return Util.createInstance(sensor);
    }

    @Nullable
    public  <T extends EffectorObserver> T lookupEffector(Class<T> effector){
        if( mEffectorsHash != null ) {
            return (T) mEffectorsHash.get(effector);
        }
        return Util.createInstance(effector);
    }

    public Properties getConfigProperties(){
        return configProp;
    }

    public Properties getMappingsProperties(){
        return mappingsProp;
    }

    public String getConfigProperty(String propName){
        return configProp.getProperty(propName);
    }

    public String getMappingsProperty(String propName){
        return mappingsProp.getProperty(propName);
    }

    public String getAccount( String typeOfAccount ){
        if( typeOfAccount.equals( Constants.GOOGLE_ACCOUNT )){
            return googleAccount;
        }
        return null;
    }

    public void setAccount( String typeOfAccount, String account ){
        if( typeOfAccount.equals( Constants.GOOGLE_ACCOUNT )){
            googleAccount = account;
        }
    }

    public static Context getContext(){
        return mContext;
    }

    @Nullable
    public DecisionRule getDecisionRule(String ruleID){
        if( mDecisionRulesHash != null ) {
            return mDecisionRulesHash.get( ruleID );
        }
        return null;
    }

    public List<DecisionRule> getDecisionRules() {
        return new ArrayList<>( mDecisionRulesHash.values() );
    }

    /**
     * It returns the rule id (if not given by the user, it will be generated automatically).
     * @param decisionRule
     * @return
     */
    public String addDecisionRule(DecisionRule decisionRule){
        mDecisionRulesHash.put(decisionRule.getRuleID(), decisionRule);
        return decisionRule.getRuleID();
    }

    public boolean removeDecisionRule(DecisionRule decisionRule){
        if( decisionRule != null ) {
            Object removed = mDecisionRulesHash.remove(decisionRule.getRuleID());
            decisionRule.destroy();
            return removed != null;
        }
        return false;
    }

    /**
     * We add all the Middleware services here.
     */
    public void addServices(ArrayList<String> requestedServices){
        // if no services are specified, then initialize all of them
        if( requestedServices == null || requestedServices.isEmpty() ){
            for( Class serviceClass : mServicesCatalog.values() ){
                mServicesHash.put( serviceClass, new MiddServiceConnection() );
            }
        }else{
            for( String serviceString : requestedServices ){
                mServicesHash.put( mServicesCatalog.get(serviceString), new MiddServiceConnection() );
            }
        }
        // we need this by default
        mServicesHash.put(mServicesCatalog.get(Constants.ADD_SERVICE_AWARE), new MiddServiceConnection());
    }

    public void addService( Class service ){
        mServicesHash.put( service, new MiddServiceConnection() );
        bindService( service );
    }

    //FIXME
    public void addSensors(){
        mSensorsHash.put( AccelerometerSensor.class, new AccelerometerSensor( new Handler(), mContext ) );
        mSensorsHash.put( SmsSensor.class, new SmsSensor( new Handler(), mContext ) );
        //... add all the sensors here

        for(SensorObserver observer : mSensorsHash.values() ){
            registerSensor(observer);
            //observer.startListening();
        }
    }

    public void addEffectors(){
        mEffectorsHash.put( AlarmEffector.class, new AlarmEffector( new Handler(), mContext ) );
        mEffectorsHash.put( SmsEffector.class, new SmsEffector( new Handler(), mContext ) );
        //... add all the effectors here

        for(EffectorObserver observer : mEffectorsHash.values() ){
            registerEffector(observer);
        }
    }

    //TODO: finish it
    public void startSensor(String name){
        if( name.equals(Constants.SENSOR_ACCELEROMETER )){
            lookupSensor(AccelerometerSensor.class).startListening();
        }else if( name.equals(Constants.SENSOR_SMS )){
            lookupSensor(SmsSensor.class).startListening();
        }
    }

    //TODO: finish it
    public void stopSensor(String name){
        if( name.equals(Constants.SENSOR_ACCELEROMETER )){
            lookupSensor(AccelerometerSensor.class).stopListening();
        }else if( name.equals(Constants.SENSOR_SMS )){
            lookupSensor(SmsSensor.class).stopListening();
        }
    }


    private void registerSensor(SensorObserver sensorObserver){
        sensorObserver.unregister(sensorDataReceiver);
        ArrayList<String> actions = sensorObserver.getActions();
        for(String action : actions ){
            if( !actionsSensorReceiver.contains(action) ){
                actionsSensorReceiver.add(action);
            }
        }
        sensorObserver.register( sensorDataReceiver, actionsSensorReceiver );
    }

    private void registerEffector(EffectorObserver effectorObserver){
        effectorObserver.unregister( effectorDataReceiver );
        ArrayList<String> actions = effectorObserver.getActions();
        for(String action : actions ){
            if( !actionsEffectorReceiver.contains(action) ){
                actionsEffectorReceiver.add(action);
            }
        }
        effectorObserver.register( effectorDataReceiver, actionsEffectorReceiver );
    }

    private void registerService(GenericService service){
        ArrayList<String> actions = service.getActions();
        if( !actions.isEmpty() ) {
            service.unregister(serviceDataReceiver);
            for (String action : actions) {
                if (!actionsServiceReceiver.contains(action)) {
                    actionsServiceReceiver.add(action);
                }
            }
            service.register(serviceDataReceiver, actionsServiceReceiver);
        }
    }

    public void bindServices(){
        for(final Class service : mServicesHash.keySet()) {
            bindService( service );
        }
    }

    public void bindService( Class service ){
        try {
            Intent intentService = new Intent(mContext, service);
            //mContext.startService(intentService);
            MiddServiceConnection connection = mServicesHash.get(service);
            mContext.bindService(intentService, connection, Context.BIND_AUTO_CREATE);
            mServicesHash.put( service, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void destroy(){
        for( MiddServiceConnection conn : mServicesHash.values() ){
            if( conn != null && conn.mService != null ) {
                conn.mService.onDestroy();
                // Unbind from the service
                if (conn.mServiceBound) {
                    mContext.unbindService(conn);
                    conn.mServiceBound = false;
                }
                conn.mService = null;
            }
        }
        for( SensorObserver sensorObserver : mSensorsHash.values() ){
            sensorObserver.stopListening();
            sensorObserver.unregister();
        }
        for( EffectorObserver effectorObserver : mEffectorsHash.values() ){
            effectorObserver.unregister();
        }

        for( MiddlewareAccessibilityServiceImpl accService : mAccessibilityServicesHash.values() ){
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && accService != null) {
                accService.getServiceImpl().disableSelf();
            }
        }

        mDecisionRulesHash = null;
        mServicesHash = null;
        mSensorsHash = null;
        mEffectorsHash = null;
        mContext = null;
        sensorDataReceiver = null;
        serviceDataReceiver = null;
        actionsSensorReceiver = null;
        actionsServiceReceiver = null;
        mClassActivitiesList = null;
        mActivitiesList = null;
        System.gc();
        active = false;
    }

    public void addActivity(Object subscriber) {
        if( subscriber != null && subscriber instanceof Activity && ( mActivitiesList.isEmpty() ||
                !mActivitiesList.get( mActivitiesList.size() -1 ).equals( subscriber ) ) ){
            mActivitiesList.add((Activity) subscriber);
            notifyActivityObservers();
        }
    }

    private void notifyActivityObservers(){
        Activity activity = getTopActivity();
        if(!(activity instanceof AccountPickerActivity)) {
            for (TopActivityObserver observer : mTopActivitiesObservers) {
                observer.notify(activity);
            }
        }
    }

    public void addTopActObserver(TopActivityObserver observer){
        if( !mTopActivitiesObservers.contains(observer) ) {
            mTopActivitiesObservers.add(observer);
        }
    }

    public void addActivityClass(Class clazz) {
        mClassActivitiesList.add( clazz );
    }

    @Nullable
    public Activity getTopActivity(){
        if( !mActivitiesList.isEmpty() ){
            return mActivitiesList.get( mActivitiesList.size() - 1 );
        }
        return null;
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public static class MiddServiceBinder extends Binder {
        private GenericService mService;

        public void setService(GenericService mService) {
            this.mService = mService;
        }

        public GenericService getService() {
            // Return this instance of Service so clients can call public methods
            return this.mService;
        }
    }

    class MiddlewareAccessibilityServiceImpl {
        private MiddlewareAccessibilityService serviceInterface;
        private AccessibilityService serviceImpl;

        public MiddlewareAccessibilityServiceImpl(MiddlewareAccessibilityService serviceInterface) {
            this.serviceInterface = serviceInterface;
        }

        public AccessibilityService getServiceImpl() {
            return serviceImpl;
        }

        public void setServiceImpl(AccessibilityService serviceImpl) {
            this.serviceImpl = serviceImpl;
        }

        public MiddlewareAccessibilityService getServiceInterface() {
            return serviceInterface;
        }

        public void setServiceInterface(MiddlewareAccessibilityService serviceInterface) {
            this.serviceInterface = serviceInterface;
        }
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private static class MiddServiceConnection implements ServiceConnection {
        private MiddServiceBinder mBinder;
        private Boolean mServiceBound;
        private GenericService mService;

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if( active ) {
                // We've bound to Service, cast the IBinder and get Service instance
                mBinder = (MiddServiceBinder) service;
                mService = mBinder.getService();
                mService.doAfterBind();
                instance.registerService(mService);
                mServiceBound = true;
                MessageBroker.getInstance(mContext).processRequests();
                numInitializedServices++;
                processAccessibilityServices();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }

        public GenericService getService() {
            return mService;
        }
    }
}
