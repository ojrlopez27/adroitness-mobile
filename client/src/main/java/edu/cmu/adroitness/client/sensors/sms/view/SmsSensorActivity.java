package edu.cmu.adroitness.client.sensors.sms.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cmu.adroitness.client.R;

import edu.cmu.adroitness.client.sensors.sms.control.ViewHelper;


public class SmsSensorActivity extends AppCompatActivity {
    private Button start;
    private Button stop;
    private TextView phoneNumber;
    private TextView message;
    private ViewHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_sms_layout);
        helper = ViewHelper.getInstance( this );
        start = (Button) findViewById( R.id.startSms);
        stop = (Button) findViewById( R.id.stopSms);
        phoneNumber = (TextView) findViewById( R.id.phoneNumber );
        message = (TextView) findViewById( R.id.message );
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                helper.startSms();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                helper.stopSms();
            }
        });
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setText( phoneNumber );
    }

    public void setMessage(String message) {
        this.message.setText( message );
    }
}
