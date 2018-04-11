package edu.cmu.adroitness.client.sensors.accelaration.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.sensors.accelaration.control.ViewHelper;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.comm.accelerometer.model.AccelerometerEvent;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;


import java.util.Date;

public class AccelerationActivity extends AppCompatActivity {

    private TextView accelerationX;
    private TextView accelerationY;
    private TextView accelerationZ;
    private TextView accuracy;
    private TextView timestamp;
    private Button startSensorBtn;
    private Button stopSensorBtn;

    private ViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_accelerometer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // controllers
        helper = ViewHelper.getInstance( this );
        MessageBroker.getInstance( this ).subscribe(this);


        accelerationX = (TextView) findViewById(R.id.accelerationX);
        accelerationY = (TextView) findViewById(R.id.accelerationY);
        accelerationZ = (TextView) findViewById(R.id.accelerationZ);
        accuracy = (TextView) findViewById(R.id.accuracy);
        timestamp = (TextView) findViewById(R.id.timestamp);

        startSensorBtn = (Button) findViewById(R.id.startAccelertometer);
        startSensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.startAccelerometer();
            }
        });

        stopSensorBtn = (Button) findViewById(R.id.stopAccelerometer);
        stopSensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.stopAccelerometer();
            }
        });
    }


    /**************************** EVENT HANDLERS **************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent( final AccelerometerEvent event ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                accelerationX.setText(event.getAccelerationX() + " m/s");
                accelerationY.setText(event.getAccelerationY() + " m/s");
                accelerationZ.setText(event.getAccelerationZ() + " m/s");
                accuracy.setText(event.getAccelerationX() + "");
                timestamp.setText(new Date(event.getTimestamp()).toString());
            }
        });
    }

}
