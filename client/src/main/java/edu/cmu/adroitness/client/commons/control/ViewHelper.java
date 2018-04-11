package edu.cmu.adroitness.client.commons.control;

import android.app.Activity;
import android.content.Intent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.comm.calendar.model.CalendarNotificationEvent;
import edu.cmu.adroitness.comm.email.model.GmailManagerEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;

/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private Activity mActivity;


    protected ViewHelper(Activity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }

        // By default, the message broker initiates all the available services
         mMB = MessageBroker.getInstance( activity );
        // Or you can initialize only those services you need:
//        ArrayList<String> services = new ArrayList();
//        services.add( Constants.ADD_SERVICE_PRIVACY );
//        services.add( Constants.ADD_SERVICE_NEWS );
//        services.add( Constants.ADD_SERVICE_ACTIVITY_RECOGNITION );
//        services.add( Constants.ADD_SERVICE_HOTEL_RESERVATION );
//        services.add( Constants.ADD_SERVICE_LOCATION );
//        services.add( Constants.ADD_SERVICE_WEATHER );
//        services.add( Constants.ADD_SERVICE_CALENDAR );
//        services.add( Constants.ADD_SERVICE_STREAMING );
//        services.add( Constants.ADD_SERVICE_TASK_UNDERSTANDING );
//        services.add( Constants.ADD_SERVICE_OAQA );
//        services.add( Constants.ADD_SERVICE_NEIL );
//        mMB = MessageBroker.getInstance( activity, services );

        mMB.subscribe(this);
    }

    public static ViewHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewHelper(activity);
        }
        return instance;
    }

    public void subscribe( Activity subscriber ){
        mMB.subscribe(subscriber);
    }
    // ****************************** COMMON *******************************************************

    public void startActivity(Class activity) {
        mMB.send( ViewHelper.this,
                MBRequest.build( Constants.MSG_LAUNCH_ACTIVITY )
                .put(Constants.BUNDLE_ACTIVITY_NAME, activity.getCanonicalName()) );
    }


    /**
     * It uploads an image to an specific server given an URL
     */
    public void uploadImage() {
        try {
            String path = "/storage/emulated/0/test.jpg";
            mMB.send(ViewHelper.this,
                    MBRequest.build(Constants.MSG_UPLOAD_TO_SERVER)
                    .put(Constants.HTTP_REQUEST_SERVER_URL, "http://128.237.182.85/AndroidFileUpload/fileUpload.php")
                    .put(Constants.HTTP_REQUEST_BODY, path)
                    .put(Constants.HTTP_RESOURCE_NAME, "image-" + System.currentTimeMillis()) //give a proper name for this image
                    .put(Constants.HTTP_FILE_EXTENSION, "jpg")
                    .put(Constants.HTTP_MIME_TYPE, "image/jpeg")
                    .put(Constants.IMG_QUALITY, 20)
                    .put(Constants.IMG_COMPRESS_FORMAT, "JPEG")
                    .put(Constants.IMG_YUV_FORMAT, true)
                    .put(Constants.IMG_WITH, 640)
                    .put(Constants.IMG_HEIGHT, 480));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // ****************************** EXTRAS ***********************************************

    /**
     * Do not implement this functionality in your code since it will kill all the processes.
     * This is just for testing purposes
     */
    public void exit(){
        mMB.destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( final CalendarNotificationEvent event ) {
        if (event.getNotification().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)) {
            this.mActivity.startActivityForResult((Intent) event.getParams(), Constants.REQUEST_AUTHORIZATION);
        }
    }

    /**
     * Event Handler. Here you can handle calendar events and error messages
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( final GmailManagerEvent event ) {
        if(event.getNotification()!=null) {
            if (event.getNotification().equals(Constants.CALENDAR_USER_RECOVERABLE_EXCEPTION)) {
                this.mActivity.startActivityForResult((Intent) event.getParams(),
                        Constants.REQUEST_AUTHORIZATION);
            }
        }
    }


}
