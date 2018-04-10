package edu.cmu.adroitness.client.sensors.audio.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yahoo.inmind.adroitness.R;

import edu.cmu.adroitness.client.sensors.audio.control.ViewHelper;


public class AudioRecordActivity extends ActionBarActivity {

    private ViewHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_streaming_activity_audio_record);
        helper = ViewHelper.getInstance( this );
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        helper.destroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    /**
     * Creates a new thread AudioStreamConsumer that subscribes to the audio stream updates
     * @param view
     */
    public void start( View view){
        helper.start( this );
    }

    /**
     * Stops the execution of the consumer thread and as a consequence, no more audio stream
     * updates are sent to this thread.
     * @param view
     */
    public void stop( View view ){
        helper.stop();
    }




    // *************************** AUTO-GENERATED ********************************************* //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu_audio_record, menu);
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
