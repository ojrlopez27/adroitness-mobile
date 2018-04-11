package edu.cmu.adroitness.client.services.red5streaming.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import edu.cmu.adroitness.comm.red5streaming.model.Red5StreamingEvent;

public class ExtendedRed5StreamingActivity extends Red5StreamingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.services_extended_red5_streaming_activity);
        showMultisenseButton.setEnabled(true);
        showMultisenseButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
    super.onClick(view);
        Log.e("Extended Activyt", "In Extended Red5 Streaming - onClick");
    }

    @Override
    protected void startStreaming()
    {
        super.startStreaming();
        Log.e("Extended Activyt", "In Extended Red5 Streaming - start streaming");
    }
    /**
     * Event Handler for Red5 streaming service
     * @param event
     */
    public void onEventMainThread(Red5StreamingEvent event){
        if( event.getRed5StreamingError() != null){
            Toast.makeText( getApplicationContext(), event.getRed5StreamingError(),
                    Toast.LENGTH_LONG).show();
        }
        if(event.getRed5StreamingServerIPAddress()!= null)
        {
            Toast.makeText( getApplicationContext(), event.getRed5StreamingServerIPAddress(),
                    Toast.LENGTH_LONG).show();
        }
     /*   if(event.getR5Stats()!= null)
        {
            Toast.makeText( getApplicationContext(), event.getResult(),
                    Toast.LENGTH_LONG).show();
        }*/
        if(event.getServiceStatus()!= null)
        {
            Toast.makeText( getApplicationContext(), event.getServiceStatus(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
