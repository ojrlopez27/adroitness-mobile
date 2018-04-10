package edu.cmu.adroitness.client.services.red5streaming.control;


import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.red5streaming.model.Red5StreamingEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.TopActivityObserver;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;
import edu.cmu.adroitness.client.services.red5streaming.view.SurfaceCameraFragment;
import edu.cmu.adroitness.client.services.generic.control.GenericService;


public class Red5StreamingService extends GenericService implements SurfaceHolder.Callback,
        TopActivityObserver, Red5StreamListener.Red5StreamListenerObserver, Red5StreamListener.Red5SurfaceViewListener {

    private int cameraSelection = 1;
    private Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    private static List<Camera.Size> sizes = new ArrayList<Camera.Size>();
    protected static String selected_item = null;

    public static Red5ConfigVO getConfig() {
        return config;
    }

    protected static Red5ConfigVO config = null;
    private R5Camera r5Cam;
    private R5Microphone r5Mic;
    private String streamingName;
    private ArrayList<String> cameraSizes = new ArrayList<String>();
    public static Red5StreamingEvent red5StreamingEvent =null;
    protected static SurfaceView surfaceForCamera, surfaceViewNotify;

    //from Streaming service
    protected Thread mCameraThread;
    protected Looper mCameraLooper;
    URL url=null;
    InetAddress inetAddress=null;

    protected String getStreamingUrl() {
        return streamingUrl;
    }

    String streamingUrl ="", rtspUrl="";

    protected static boolean wasStreaming=false, isStopStreaming=false,
            isStopCamera=false, isPrepareCamera=false, enableToggleCamera=true, isSurfaceDestroyed=false;



    Red5StreamListener red5StreamListener, notifyRed5StreamListener;

    public int getLayoutID() {
        return layoutID;
    }

    public Red5StreamingService() {
        super(null);
        if(actions.isEmpty())
        {
            this.actions.add(Constants.ACTION_RED5STREAMING);
        }
        red5StreamListener = new Red5StreamListener();
        layoutID = View.generateViewId();
    }

    static {
        if(config==null){
            config = new Red5ConfigVO();
        }
    }

    protected static Camera camera;
    protected static boolean isStreaming = false;

    R5Stream stream;
    public final static String TAG = "Preview";

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    /***
     * The method is called by Start button click.
     * The method starts the streaming connectiong to the host server on
     * AWS EC2 over RTSP  port 8554. The phone camera is attached to R5Cam and R5Stream
     * bit rate and resolution is only for R5Camera for streaming the bits
     * @param surfaceView
     * @param config
     */
    public void startStreaming(final SurfaceView surfaceView, final Red5ConfigVO config) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("StreamingService", "*** inside startStreaming");
                if(camera==null) {
                    prepareCamera(surfaceView, config);
                }
                if(Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Red5StreamingService.this.config = config;
                Red5StreamingService.this.surfaceForCamera = surfaceView;

                if(!isStreaming) {
                    R5Configuration rConfig = new R5Configuration(R5StreamProtocol.RTSP, config.getHost(),
                            config.getPort(), config.getApp(), 1.0f);
                    rConfig.setLicenseKey("JC7B-X52T-G4TN-GG74");
                    R5Connection rConn = new R5Connection(rConfig);
                    stream = new R5Stream(rConn);
                    stream.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

                    stream.connection.addListener(new R5ConnectionListener() {
                        @Override
                        public void onConnectionEvent(R5ConnectionEvent event) {
                            Log.d("publish","connection event code "+event.value()+"\n");
                            switch(event.value()){
                                case 0://open
                                    Log.d("red5Event",event.message);
                                    break;
                                case 1://close
                                    Log.d("red5Event",event.message);
                                    break;
                                case 2://error
                                    Log.d("red5Event",event.message);
                                    break;

                            }
                        }
                    });

                    stream.setListener(new R5ConnectionListener() {
                        @Override
                        public void onConnectionEvent(R5ConnectionEvent event) {
                            switch (event) {
                                case CONNECTED:

                                     red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_CONNECTED);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    break;
                                case DISCONNECTED:
                                     red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_DISCONNECTED);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    break;
                                case START_STREAMING:
                                    streamingUrl = "https://"
                                    +config.getHost()+":5080/"
                                            +config.getApp()+"/flash.jsp?host="
                                            +config.getHost()+"&stream="
                                            +config.getName()+"\n";
                                    red5StreamListener.setStreamName(config.getName());
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_START_STREAMING);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    Log.d("Red5", "START_STREAMING SUCCESS");
                                    break;
                                case STOP_STREAMING:
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_STOP_STREAMING);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    wasStreaming = true;
                                    break;
                                case CLOSE:
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_CLOSE);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    wasStreaming = true;
                                    break;
                                case TIMEOUT:
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setServiceStatus(Constants.RED5_STREAMING_STATUS_TIMEOUT);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    break;
                                case ERROR:
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setRed5StreamingError(Constants.RED5_STREAMING_STATUS_ERROR);
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    Log.d("Red5StreamError",event.message);
                                    break;
                                case NET_STATUS:
                                    red5StreamingEvent = Red5StreamingEvent.build()
                                            .setR5Stats(stream.getStats());
                                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                                    break;
                            }
                        }
                    });

                    camera.stopPreview();

                    //assign the surface to prepare the camera output
                    Red5StreamingService.this.surfaceForCamera = surfaceView;
                    stream.setView(surfaceForCamera);

                    //add the camera for streaming
                    if(selected_item != null) {
                        Log.d("publisher","selected_item "+selected_item);
                        String bits[] = selected_item.split("x");
                        int pW= Integer.valueOf(bits[0]);
                        int pH=  Integer.valueOf(bits[1]);
                        if((pW/2) %16 !=0){
                            pW=320;
                            pH=240;
                        }
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setPreviewSize(pH, pW);
                        camera.setParameters(parameters);
                        setDisplayOrientation();
                        r5Cam = new R5Camera(camera,pH,pW);
                        r5Cam.setBitrate(config.getBitrate());
                    }
                    else {
                        Camera.Parameters parameters = camera.getParameters();
                        camera.setDisplayOrientation(180);
                        parameters.setPreviewSize(320, 240);
                        camera.setParameters(parameters);
                        setDisplayOrientation();
                        //parameters.set("orientation", "portrait");
                        r5Cam = new R5Camera(camera,240,320);
                        r5Cam.setBitrate(config.getBitrate());
                    }

                    if(cameraSelection==1) {
                        r5Cam.setOrientation(90 );
                    }
                    else {
                        r5Cam.setOrientation(90);
                    }
                    r5Mic = new R5Microphone();


                    if(config.getVideo()) {
                        stream.attachCamera(r5Cam);
                    }

                    if(config.getAudio()) {
                        stream.attachMic(r5Mic);
                    }

                    isStreaming = true;
                    isPrepareCamera=false;
                    isStopStreaming=false;
                    isStopCamera=false;
                    wasStreaming = false;
                    stream.publish(config.getName(), R5Stream.RecordType.Live);
                    streamingName = config.getName();
                    camera.startPreview();
                    red5StreamListener.startListener(stream);
                    if(notifyRed5StreamListener==null) {
                        notifyRed5StreamListener = getRed5StreamListener();
                        notifyRed5StreamListener.subscribeToListener(Red5StreamingService.this);
                    }
                    else
                    {
                        notifyRed5StreamListener.stream.removeListener();
                        notifyRed5StreamListener = getRed5StreamListener();
                        notifyRed5StreamListener.subscribeToListener(Red5StreamingService.this);
                    }
                }
                resourceLocator.popRequest( Red5StreamingService.class );
                Log.d("StreamingService", "*** end of startStreaming");
            }
        });
    }

    /***
     * Called by Stop button click along with other methods
     * Red5 Stream is stopped with this method. All it needs is the stream object
     */
    public void stopStreaming() {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("StreamingService", "*** inside stopStreaming");
                if(!isStopStreaming && isStreaming) {
                    isStopStreaming = true;
                    if (stream != null) {
                        stream.stop();
                    }
                    if (camera != null) {
                        surfaceForCamera.getHolder().removeCallback(Red5StreamingService.this);
                        Red5StreamingService.this
                                .surfaceDestroyed(Red5StreamingService.this.surfaceForCamera.getHolder());
                    }
                    isStreaming = false;
                    wasStreaming = true;
                    Log.d(TAG, "stop streaming in progress");
                }
                resourceLocator.popRequest( Red5StreamingService.class );
                Log.d("StreamingService", "*** end of stopStreaming");
            }
        });
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            if(camera!= null) {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                Log.d("SurfaceCreated","Surface Created- Red5 Streaming Activity");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /***
     * Method is called at the start of activity as well as in Resume state.
     * Camera preview is hidden and since camera service can be in use
     * and/or to avoid use by other services, A semaphore is used which
     * disposes of any existing connections and starts a new camera service.
     * Semaphore lock from old Streaming service.
     * @param surface
     * @param configVO
     */
    private synchronized void prepareCamera(final SurfaceView surface , final Red5ConfigVO configVO) {
        Log.d("StreamingService", "*** inside prepareCamera");
        final Semaphore lock = new Semaphore(0);
        final RuntimeException[] exception = new RuntimeException[1];
        resourceLocator.addTopActObserver(this);
        if (surface != null) {
            Red5StreamingService.this.surfaceForCamera = surface;
            Log.d("SurfaceView", Red5StreamingService.this.surfaceForCamera.getHolder().toString());
        }
        if (config != null) {
            Red5StreamingService.this.config = configVO;

            Log.d("ConfigVO", Red5StreamingService.this.config.getName());
        }

        mCameraThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mCameraLooper = Looper.myLooper();
                try {
                    if (camera != null) {
                        surfaceForCamera.getHolder().removeCallback(Red5StreamingService.this);
                        sizes.clear();
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                    int num = Camera.getNumberOfCameras();
                    if (num == 1) {
                        cameraSelection = 0;
                        enableToggleCamera = false;

                    }
                    Camera.getCameraInfo(cameraSelection, cameraInfo);
                    camera = Camera.open(cameraSelection);
                    camera.setDisplayOrientation(270);
                    isStopCamera = false;
                    sizes = camera.getParameters().getSupportedPreviewSizes();
                    Log.d("sizes", String.valueOf(Red5StreamingService.sizes.size()));
                    if (Red5StreamingService.this.surfaceForCamera.getHolder().isCreating()) {
                        Red5StreamingService.this.surfaceForCamera.getHolder()
                                .addCallback(Red5StreamingService.this);
                    } else {
                        Red5StreamingService.this.surfaceForCamera.getHolder()
                                .addCallback(Red5StreamingService.this);

                        Red5StreamingService.this
                                .surfaceCreated(Red5StreamingService.this.surfaceForCamera.getHolder());
                    }
                    cameraSizes = new ArrayList<>();
                    for (Camera.Size size : Red5StreamingService.sizes) {
                        if ((size.width / 2) % 16 != 0) {
                            continue;
                        }
                        String potential = String.valueOf(size.width).trim() + "x"
                                + String.valueOf(size.height).trim();
                        cameraSizes.add(potential);
                    }
                    isPrepareCamera = true;
                    Log.d("cameraSizes", String.valueOf(cameraSizes.size()));

                } catch (RuntimeException e) {
                    exception[0] = e;
                } finally {
                    lock.release();
                    resourceLocator.popRequest(Red5StreamingService.class);
                    Looper.loop();
                }
            }
        });
        mCameraThread.start();
        lock.acquireUninterruptibly();
        Log.d("cameraSizes",String.valueOf(cameraSizes.size()));
        if (exception[0] != null) throw new IllegalStateException(exception[0].getMessage());
        Log.d("StreamingService", "*** end of prepareCamera");
    }

    /***
     * Get resolutions/sizes which were populated at the time of prepareCamera.
     * @return
     */
    public  ArrayList<String> getCameraSizes(){
        Log.d("GetCameraSizes", String.valueOf(Red5StreamingService.this.cameraSizes.size()));
        return cameraSizes;
    }

    /***
     * Called by Stop button click to stop camera.
     * Also called by Toggle camera (camera button click).
     */
    public void stopCamera() {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if(camera != null) {
                    //surfaceViewNotify.getHolder().removeCallback( Red5StreamingService.this);
                    sizes.clear();
                    camera.stopPreview();
                    try {
                        camera.setPreviewDisplay(null);
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                    camera.release();
                    isStopCamera = true;
                    camera = null;
                }
            }
        });
    }

    /***
     * Called by Camera button for toggle operation
     */
    public void toggleCamera() {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if(!enableToggleCamera) {
                    cameraSelection = (cameraSelection + 1) % 2;
                    try {
                        Camera.getCameraInfo(cameraSelection, cameraInfo);
                        cameraSelection = cameraInfo.facing;
                    } catch (Exception e) {
                        // can't find camera at that index, set default 
                        cameraSelection = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }

                    Red5StreamingEvent red5StreamingEvent = Red5StreamingEvent.build()
                            .setServiceStatus("Red5 Streaming Camera was toggled!");
                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                    stopCamera();
                    prepareCamera(surfaceForCamera, config);
                }
            }
        });

    }

    public String getStreamingName() {
        return streamingName;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        if(camera!=null) {
            camera.stopPreview();
            try
            {
                camera.setPreviewDisplay(null);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            camera.release();
            sizes.clear();
            isStopCamera = true;
            camera = null;
            Log.d("SurfaceDestroyed","Surface destroyed!");

            this.notifySurfaceDestroyed(surfaceViewNotify);
        }
        else
        {
            this.notifySurfaceDestroyed(surfaceViewNotify);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3){
        Log.d("SurfaceChanged","Surface changed!");

    }

    public String getIPAddressOfRed5Host(String urlString)
    {
        urlString ="http://devstreaming.ddns.net:5080/";
        try
        {
            url =  new URL(urlString);
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        Util.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    inetAddress = InetAddress.getByName(url.getHost());

                    Red5StreamingEvent red5StreamingEvent = Red5StreamingEvent.build()
                            .setServiceStatus("Red5 Streaming server IP Address is "+ inetAddress)
                            .setRed5StreamingServerIPAddress(inetAddress.toString());
                    mb.send(Red5StreamingService.class, red5StreamingEvent);
                }
                catch(UnknownHostException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return inetAddress.toString().substring(inetAddress.toString().indexOf('/')+1);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if(!isStreaming)
                {

                }
            }
        });
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    RelativeLayout layout;
    SurfaceView surfaceView;
    int activityCount=0;
    static SurfaceCameraFragment surfaceCameraFragment;
    FragmentTransaction fragmentTransaction;
    HashMap<String, Integer> activityLayoutMap = new HashMap<>();
    static boolean isNotify=false, isFirstTime=false;
    static RelativeLayout relativeLayout;
    static LinearLayout linearLayout;
    public static Activity notifiedActivity;
    protected int layoutID=0;
    @Override
    public void notify(final Activity activity)
    {
        isNotify=true;
        Log.d("notify","*** inside notify. Activity is: " +activity.getLocalClassName());
        isNotify = true;
        notifiedActivity = activity;
       // attachFragment(activity);
    }

    public synchronized void attachFragment(MBRequest request)
    {
        final Activity activity = (Activity) request.get(Constants.RED5STREAMING_ACTIVITY);
        final String isManualStreaming = (String) request.get(Constants.RED5STREAMING_FLAG);
        Util.execute(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("StreamingService",activity.getClass().getSimpleName());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, 1);
                        linearLayout = new LinearLayout(activity);
                        linearLayout.setLayoutParams(layoutParams);
                        linearLayout.setId(layoutID);
                        activity.addContentView(linearLayout, layoutParams);
                        surfaceCameraFragment = SurfaceCameraFragment.newInstance(isManualStreaming,"");
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(linearLayout.getId(),
                                    surfaceCameraFragment,
                                String.valueOf(System.currentTimeMillis())).commit();
                        Log.d("StreamingService","attach camera fragment");

                    }
                });
            }
        });
    }



    @Override
    public synchronized void notifySurfaceDestroyed(SurfaceView surfaceView) {
        if(isNotify && notifiedActivity!=null) {
            attachFragment(MBRequest.build(Constants.RED5STREAMING_START_STREAMING)
                            .put( Constants.RED5STREAMING_ACTIVITY, notifiedActivity)
                            .put( Constants.RED5STREAMING_FLAG, new Boolean("false")));
            Log.d("StreamingService", "notifySurfaceDestroyed");
        }
    }

    public void createConfigVO()
    {
        config = new Red5ConfigVO();
        config.setHost("34.203.204.136");
        config.setPort(8554);
        config.setApp("live");
        //TODO
        config.setName(streamingName); // + Util.getDeviceId( mActivity.getApplicationContext() ) ); // it should be unique
        config.setBitrate(128);
        config.setAudio(false);
        config.setVideo(true);
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        if(isStreaming) {
            stopStreaming();
            if(camera!=null) {
                camera.stopPreview();
                try
                {
                    camera.setPreviewDisplay(null);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                camera.release();
                sizes.clear();
                isStopCamera = true;
                camera = null;
                Log.d("SurfaceDestroyed","Surface destroyed!");

                //this.notifySurfaceDestroyed(surfaceViewNotify);
            }
        }
    }

    public Red5StreamListener getRed5StreamListener() {
        return red5StreamListener;
    }
    public boolean isStreaming() {
        return isStreaming;
    }

    @Override
    public void onStreamEvent(Red5StreamListener listener) {
        Log.d("notify","Notify Red5 listener "+ listener.getStreamState());

        if(listener.getStreamState().equals(Constants.RED5_STREAMING_STATUS_DISCONNECTED))
        {
            if(isNotify) {


            }
        }
    }

    public static void sendRed5StreamingEvent(String streamState)
    {
        red5StreamingEvent = Red5StreamingEvent.build()
                .setServiceStatus(streamState);
        mb.send(Red5StreamingService.class, red5StreamingEvent);
    }

    /***
     * Set Display Orientation to show preview in portrait mode
     */
    private void setDisplayOrientation()
    {
        if(camera!=null) {
            Camera.Parameters parameters = camera.getParameters();
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            int or = cameraInfo.orientation;
            // You need to choose the most appropriate previewSize for your app
            // .... select one of previewSizes here
           /* parameters.setPreviewSize(previewSize.width, previewSize.height);*/
            if (display.getRotation() == Surface.ROTATION_0) {

                camera.setDisplayOrientation(90);
                or = 90;
            }

            if (display.getRotation() == Surface.ROTATION_180) {
                camera.setDisplayOrientation(270);
                or = 270;
            }
            if (display.getRotation() == Surface.ROTATION_270) {
                camera.setDisplayOrientation(180);
                or = 180;
            }

            parameters.setRotation(or);
            camera.setParameters(parameters);
        }
    }

}
