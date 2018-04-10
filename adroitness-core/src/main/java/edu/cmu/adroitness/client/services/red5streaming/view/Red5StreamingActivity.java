package edu.cmu.adroitness.client.services.red5streaming.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.red5streaming.model.Red5StreamingEvent;
import edu.cmu.adroitness.client.services.red5streaming.model.Red5ConfigVO;

import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamingController;

public class Red5StreamingActivity extends AppCompatActivity implements View.OnClickListener,
        SettingsDialogFragment.OnFragmentInteractionListener{ //, SurfaceHolder.Callback {

    protected Button startBtn, stopBtn, showMultisenseButton;
    protected ImageButton cameraButton;
    protected static String selected_item = null;
    protected static int preferedResolution = 0;
    protected static SurfaceView surfaceForCamera;
    private Red5StreamingController red5StreamingController;
    protected boolean isStreaming = false;
    protected final static String TAG = "Red5StreamingActivity";
    protected static TextView logTxtView;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    protected static Camera camera;
    protected SurfaceCameraFragment surfaceCameraFragment;
    FragmentTransaction fragmentTransaction;
    //public static SurfaceHolder surfaceHolder;
    protected static ArrayList<String> cameraSizes = new ArrayList<String>();
    protected static Red5ConfigVO red5ConfigVO;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.services_red5_streaming_activity_main);
        red5StreamingController = Red5StreamingController.getInstance(this);
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        context = this;

        relativeLayout = (RelativeLayout) findViewById(R.id.red5_layout);
        cameraButton = (ImageButton)findViewById(R.id.btnCamera);
        startBtn = (Button)findViewById(R.id.button);
        showMultisenseButton = (Button) findViewById(R.id.showMultisenseBtn);

         //surfaceForCamera  = (SurfaceView)findViewById(R.id.surfaceView);

        /*** add camera fragment ***/
        red5StreamingController.attachFragment(this, "true");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, 1);
        /*linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(layoutParams);
        String activityClassName = this.getClass().getSimpleName();
        linearLayout.setId(View.generateViewId());
        this.addContentView(linearLayout, layoutParams);
        surfaceCameraFragment = SurfaceCameraFragment.newInstance("true","");
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(linearLayout.getId(), surfaceCameraFragment, "Red5").commit();*/


        stopBtn = (Button) findViewById(R.id.button2);
        logTxtView = (TextView) findViewById(R.id.logTxtView);
        logTxtView.setMovementMethod(new ScrollingMovementMethod());
        logTxtView.append(String.format("Streaming Status Logger has been started!\n"));
        showMultisenseButton.setVisibility(View.INVISIBLE);
        showMultisenseButton.setEnabled(false);

        configure();
        //viewHelper.prepareCamera( surfaceForCamera, viewHelper.getConfigVO() );
    }

    public String getStringResource(int id) {
        return getResources().getString(id);
    }

    protected void toggleCamera() {
        red5StreamingController.toggleCamera();
        Red5StreamingActivity.logTxtView.append(String.format("Camera display has been updated!\n"));
    }

    @Override
    protected void onResume() {

        System.out.print("On Resume Red5Streaming Activity");
        MessageBroker.getInstance( getApplicationContext() ).subscribe(this);
        //surfaceForCamera = SurfaceCameraFragment.getSurfaceView();

        /*if((((RelativeLayout) this.findViewById(R.id.red5_layout)).findViewById(R.id.surfaceViewNotify)) instanceof SurfaceView)
        {
            System.out.print("in Red5 Streaming Activity onResume");
        }
        else
        {
            ((RelativeLayout) this.findViewById(R.id.red5_layout)).addView(surfaceForCamera);
            System.out.print("in Red5 Streaming Activity onResume - added back");
        }*/

        //ResourceLocator.getInstance(this);
        showMultisenseButton.setEnabled(false);
        showMultisenseButton.setVisibility(View.INVISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.services_red5_streaming_activity_main);

    }

    @Override
    protected void onPause() {
        //((RelativeLayout) this.findViewById(R.id.red5_layout)).removeView(surfaceForCamera);
        System.out.print("in Red5 Streaming Activity onPause");
        MessageBroker.getInstance( getApplicationContext() ).unsubscribe(this);
        //this.moveTaskToBack(true);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        System.out.print("in Red5StreamingActivity OnDestroy");
        red5StreamingController.stopStreaming();
        MessageBroker.getInstance( getApplicationContext() ).unsubscribe(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button2) {
            stopStreaming();
        }else if(view.getId() == R.id.button) {
            startStreaming();
        }else if(view.getId() == R.id.btnCamera) {
            toggleCamera();
        }
        else if(view.getId() == R.id.btnSettings) {
            openSettings();
        }
        if(view.getId() == R.id.showMultisenseBtn) {
            startMultisense();
        }

    }

    protected void startMultisense()
    {
        Log.d(TAG, "In Red5StreamingActivity");
    }
    public void onStateSelection(AppState state) {
        this.finish();
    }

    public void onSettingsClick() {
        openSettings();
    }
    protected void startStreaming()
    {
        if(!isStreaming){
            //new Red5StreamingStart().execute(surfaceForCamera);
            //red5StreamingController.prepareCamera( surfaceForCamera, red5StreamingController.getConfigVO() );
            surfaceForCamera = SurfaceCameraFragment.getSurfaceView();
            red5StreamingController.startStreaming(red5StreamingController.getConfigVO(), surfaceForCamera);
            startBtn.setText("Recording");
            startBtn.setTextColor(Color.RED);
            cameraButton.setVisibility(View.GONE);
            isStreaming=true;

        }
    }

    protected void stopStreaming()
    {
        if (isStreaming) {
            red5StreamingController.stopStreaming();
            startBtn.setText("Start");
            startBtn.setTextColor(Color.BLACK);
            cameraButton.setVisibility(View.VISIBLE);
            isStreaming=false;
            Red5StreamingActivity.logTxtView.append(String.format("Stream "+
                    red5StreamingController.getConfigVO().getName()+ " has been stopped!\n"));
        }
    }
    /***
     * Everytime settings dialog is closed, call configure to check if config VO object is null.
     * If null, set to default values. Config object can be null only when phone memory was full
     */
    public void onSettingsDialogClose() {
        configure();
    }

    /***
     * Settings dialog fragment is for Red5 Settings edit.
     * Additionally resolution values are loaded in Settings dialog based on camera toggle.
     * Called at the time of start activity or when user clicks on Settings button.
     */
    private void openSettings(){
        try {
            SettingsDialogFragment newFragment =
                    SettingsDialogFragment.newInstance(AppState.PUBLISH);
            newFragment.show(getFragmentManager().beginTransaction(), "settings_dialog");
            if(cameraSizes.isEmpty()) {
                cameraSizes = red5StreamingController.getCameraSizes();
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_spinner_item, cameraSizes) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(0xffff0000);
                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTextColor(0xffff0000);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    return v;
                }
            };
                newFragment.setSpinnerAdapter(adapter);

        }catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Can't open settings: " + e.getMessage());
        }
    }

    /***
     * Red5 Configuration details for Red5Configuration object is instantiated here.
     * Default host, port, App name, stream name, bitrate, Audio flag and Video flag.
     * This is called at the start of activity.
     */
    private void configure() {

        if(!red5StreamingController.flag || red5StreamingController.getConfigVO() == null) {
            red5StreamingController.createConfigVO();
        }
        red5ConfigVO = new Red5ConfigVO();
        red5ConfigVO = red5StreamingController.getConfigVO();
    }
    /*
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3)
    {

    }*/

    public Red5StreamingController getViewHelper()
    {
        return red5StreamingController;
    }

    /**
     * Event Handler for Red5 streaming service
     * @param event
     */
    public void onEventMainThread(final Red5StreamingEvent event){
        if( event.getRed5StreamingError() != null){

            if(!event.getRed5StreamingError().equals(""))
            {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.print("Red5 Activity: "+event.getRed5StreamingError());
                        Toast.makeText( getApplicationContext(), event.getRed5StreamingError(),
                                Toast.LENGTH_LONG).show();
                        logTxtView.append(String.format("Stream "
                                + red5StreamingController.getConfigVO().getName()
                                + " has been started successfully!\n"));

                        logTxtView.append(
                                String.format("You can verify Streaming at host: https://"
                                        +red5StreamingController.getConfigVO().getHost()+":5080/"
                                        +red5StreamingController.getConfigVO().getApp()+"/flash.jsp?host="
                                        +red5StreamingController.getConfigVO().getHost()+"&stream="
                                        +red5StreamingController.getConfigVO().getName()+"\n"));
                    }
                });
                }
        }
        if(event.getRed5StreamingServerIPAddress()!= null)
        {
            if(!event.getRed5StreamingServerIPAddress().equals("")) {
                Toast.makeText(getApplicationContext(), event.getRed5StreamingServerIPAddress(),
                        Toast.LENGTH_LONG).show();
            }
        }
        if(event.getR5Stats()!= null)
        {
           // Toast.makeText( getApplicationContext(), event.getR5Stats().toString(), Toast.LENGTH_LONG).show();
        }
        if(event.getServiceStatus()!= null)
        {
            if(!event.getServiceStatus().equals(""))
            {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.print("Red5 Activity: "+event.getServiceStatus());
                        Toast.makeText( getApplicationContext(), event.getServiceStatus(),
                                Toast.LENGTH_LONG).show();
                        logTxtView.append(String.format("Stream "
                                + red5StreamingController.getConfigVO().getName()
                                + " has been started successfully!\n"));

                        logTxtView.append(
                                String.format("You can verify Streaming at host: https://"
                                        +red5StreamingController.getConfigVO().getHost()+":5080/"
                                        +red5StreamingController.getConfigVO().getApp()+"/flash.jsp?host="
                                        +red5StreamingController.getConfigVO().getHost()+"&stream="
                                        +red5StreamingController.getConfigVO().getName()+"\n"));
                    }
                });
            }
        }
    }
}
