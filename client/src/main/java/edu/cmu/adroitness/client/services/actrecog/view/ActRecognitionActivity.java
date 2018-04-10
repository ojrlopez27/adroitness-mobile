package edu.cmu.adroitness.client.services.actrecog.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.services.activity.view.ARContextCard;
import edu.cmu.adroitness.client.services.activity.view.ActivityRecognitionSettings;
import edu.cmu.adroitness.client.services.actrecog.control.ViewHelper;
import edu.cmu.adroitness.client.services.generic.view.ServicesActivity;
import edu.cmu.adroitness.client.services.location.view.LocationContextCard;
import edu.cmu.adroitness.client.services.location.view.LocationSettings;
import edu.cmu.adroitness.comm.accelerometer.model.AccelerometerEvent;
import edu.cmu.adroitness.comm.ar.model.ActivityRecognitionEvent;
import edu.cmu.adroitness.comm.location.model.LocationEvent;


public class ActRecognitionActivity extends ServicesActivity {
    private FrameLayout frame;
    private FrameLayout frame2;
    private Button button;
    private Button button2;
    private TextView tv;
    private ViewHelper helper;
    private LocationContextCard locationCard;
    private ARContextCard activityCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_act_recog_activity);

        frame = (FrameLayout) findViewById(R.id.frameLocation);
        frame2 = (FrameLayout) findViewById(R.id.frameActivity);
        tv = (TextView) findViewById(R.id.tv);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
        lp.gravity = Gravity.CENTER;

        frame.addView( locationCard.getContextCard(this), lp);
        frame2.addView( activityCard.getContextCard(this));

        button = (Button) findViewById(R.id.locationPlugin);
        button2 = (Button) findViewById(R.id.activityPlugin);
        helper = ViewHelper.getInstance( this );
    }

    public void onResume(){
        super.onResume();
        locationCard.registerReceiver( this );

//        boolean isActiveFused = helper.isLocationActive();
//        boolean isActiveActivity = helper.isActRecognitionActive();
//
//        Log.d("aa", isActiveFused + " " + isActiveActivity);
//        if(isActiveFused){
//            button.setText("Stop Google Fused Location");
//        }else {
//            button.setText("Start Google Fused Location");
//        }
//        if(isActiveActivity){
//            button2.setText("Stop Activity Recognition");
//        }else {
//            button2.setText("Start Activity Recognition");
//        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        locationCard.unregisterReceiver(this);
    }

    public void onEvent( ActivityRecognitionEvent event){
        tv.setText("Current Activity: " + helper.getActivityRecognitionName(event.getActivityType()) + " with Confidence: %" + event.getConfidence());
    }

    /**
     * For testing purposes
     * @param event
     */
    public void onEvent( LocationEvent event){
        //for testing purposes. Implement a proper GUI to show the results
    }

    /**
     * For testing purposes
     * @param event
     */
    public void onEvent( AccelerometerEvent event ){
        // ...
    }


    public void settingsLocation(View v) {
        Intent settings = new Intent(this, LocationSettings.class);
        startActivity(settings);
    }

    public void settingsActivity(View v) {
        Intent settings = new Intent(this, ActivityRecognitionSettings.class);
        startActivity(settings);
    }

    public void locationPlugin(View v) {
        if(button.getText().toString().equals("Start Google Fused Location")){
            //helper.startPlugin( Constants.SERVICE_LOCATION );
            button.setText("Stop Google Fused Location");
        }else{
            //helper.stopPlugin(Constants.SERVICE_LOCATION);
            button.setText("Start Google Fused Location");
        }
    }

    public void activityPlugin(View v) {
        if(button2.getText().toString().equals("Start Activity Recognition")){
            //helper.startPlugin(Constants.SERVICE_AR);
            button2.setText("Stop Activity Recognition");
        }else{
            //helper.stopPlugin(Constants.SERVICE_AR);
            button2.setText("Start Activity Recognition");
        }
    }
    public void onDestroy(){
        super.onDestroy();
    }


    // *********************************** AUTO-GENERATED ****************************************//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plugins, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
