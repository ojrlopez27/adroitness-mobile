package edu.cmu.adroitness.client.effectors.sms.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.effectors.sms.control.ViewHelper;
import edu.cmu.adroitness.client.effectors.sms.model.SmsVO;

public class SmsEffectorActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText txtphoneNo;
    private EditText txtMessage;
    private ViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effectors_sms_layout);
        helper = ViewHelper.getInstance( this );

        sendBtn = (Button) findViewById(R.id.sendSMSButton);
        txtphoneNo = (EditText) findViewById(R.id.phoneNumberText);
        txtMessage = (EditText) findViewById(R.id.messageText);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SmsVO smsVO = new SmsVO();
                smsVO.setPhoneNumber( txtphoneNo.getText().toString() );
                smsVO.setMessage( txtMessage.getText().toString() );
                helper.sendSMS( smsVO );
            }
        });
    }
}
