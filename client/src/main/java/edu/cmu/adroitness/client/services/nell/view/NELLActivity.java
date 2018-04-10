package edu.cmu.adroitness.client.services.nell.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.nell.control.ViewHelper;

public class NELLActivity extends AppCompatActivity {

    private ViewHelper helper;
    private EditText entityOrText;
    private EditText results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_nell_main);
        helper = ViewHelper.getInstance( this );
        entityOrText = (EditText) findViewById(R.id.entityText);
        results = (EditText) findViewById(R.id.resultsNELL);
    }

    public EditText getResults() {
        return results;
    }

    public void runMacroReading(View view){
        helper.runMacroReading( entityOrText.getText().toString() );
    }

    public void runMicroReading(View view){
        helper.runMicroReading( entityOrText.getText().toString() );
    }

    @Override
    protected void onResume(){
        helper.subscribe();
        super.onResume();
    }

    @Override
    protected void onPause(){
        helper.unsubscribe();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        helper.unsubscribe();
        super.onDestroy();
    }
}
