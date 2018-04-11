package edu.cmu.adroitness.client.services.rules.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.UIutil;
import edu.cmu.adroitness.client.services.rules.control.ViewHelper;

public class DecisionRuleActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private ViewHelper helper;
    private Spinner condCompName;
    private Spinner condAttribute;
    private Spinner condOperator;
    private Spinner condRefValue;
    private Spinner accCompName;
    private Spinner accAttribute1;
    private Spinner accValue1;
    private Spinner accAttribute2;
    private Spinner accValue2;
    private Spinner accAttribute3;
    private Spinner accValue3;
    private EditText condValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_decision_rule_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        condCompName = (Spinner) findViewById(R.id.componentCondSpinner);
        condAttribute = (Spinner) findViewById(R.id.attributeCondSpinner);
        condOperator = (Spinner) findViewById(R.id.operatorCondSpinner);
        condRefValue = (Spinner) findViewById(R.id.refCondSpinner);
        accCompName = (Spinner) findViewById(R.id.componentAccSpinner);
        accAttribute1 = (Spinner) findViewById(R.id.attribute1AccSpinner);
        accAttribute2 = (Spinner) findViewById(R.id.attribute2AccSpinner);
        accAttribute3 = (Spinner) findViewById(R.id.attribute3AccSpinner);
        accValue1 = (Spinner) findViewById(R.id.val1AccSpinner);
        accValue2 = (Spinner) findViewById(R.id.val2AccSpinner);
        accValue3 = (Spinner) findViewById(R.id.val3AccSpinner);
        condValue = (EditText) findViewById(R.id.valueCondEdit);

        UIutil.populateSpinner( this, condCompName, R.array.condition_component);
        UIutil.populateSpinner( this, condOperator, R.array.operators);
        UIutil.populateSpinner( this, condRefValue, R.array.ref_value);
        UIutil.populateSpinner( this, accCompName, R.array.action_component);

        // controllers
        helper = ViewHelper.getInstance( this );
        //helper.createCrowdRules();
    }

    public void generateRules(View view){
        //helper.createCrowdRules();
        helper.generateRules(
                condCompName.getSelectedItem(),
                condAttribute.getSelectedItem(),
                condOperator.getSelectedItem(),
                condValue.getText().toString(),
                condRefValue.getSelectedItem(),
                accCompName.getSelectedItem(),
                accAttribute1.getSelectedItem(),
                accValue1.getSelectedItem(),
                accAttribute2.getSelectedItem(),
                accValue1.getSelectedItem(),
                accAttribute3.getSelectedItem(),
                accValue3.getSelectedItem()
        );
    }

    public void removeRules(View view){
        helper.removeCrowdRules();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if( parent.getId() == R.id.componentCondSpinner ){
            if( parent.getSelectedItem().equals("ACCELEROMETER") ){
                UIutil.populateSpinner( this, condAttribute, R.array.accelerometer_attributes );
            }else if( parent.getSelectedItem().equals("CALENDAR") ){
                UIutil.populateSpinner( this, condAttribute, R.array.calendar_attributes );
            }
        }else if( parent.getId() == R.id.componentAccSpinner ){
            if( parent.getSelectedItem().equals("ALARM") ){
                UIutil.populateSpinner( this, accAttribute1, R.array.alarm_attributes );
                UIutil.populateSpinner( this, accAttribute2, R.array.alarm_attributes );
                UIutil.populateSpinner( this, accAttribute3, R.array.alarm_attributes );
                UIutil.populateSpinner( this, accValue1, R.array.alarm_values );
                UIutil.populateSpinner( this, accValue2, R.array.alarm_values );
                UIutil.populateSpinner( this, accValue3, R.array.alarm_values );
            }else if( parent.getSelectedItem().equals("TOAST") ){
                UIutil.populateSpinner( this, accAttribute1, R.array.toast_attributes );
                UIutil.populateSpinner( this, accAttribute2, R.array.toast_attributes );
                UIutil.populateSpinner( this, accAttribute3, R.array.toast_attributes );
                UIutil.populateSpinner( this, accValue1, R.array.toast_values );
                UIutil.populateSpinner( this, accValue2, R.array.toast_values );
                UIutil.populateSpinner( this, accValue3, R.array.toast_values );
            }
        }else if( parent.getId() == R.id.attribute1AccSpinner && accAttribute1.getSelectedItem().equals("ALARM_CONDITION_AT")  ){
            UIutil.populateSpinner( this, accValue1, R.array.hours );
        }else if( parent.getId() == R.id.attribute2AccSpinner && accAttribute2.getSelectedItem().equals("ALARM_CONDITION_AT")){
            UIutil.populateSpinner( this, accValue2, R.array.hours );
        }else if( parent.getId() == R.id.attribute3AccSpinner && accAttribute3.getSelectedItem().equals("ALARM_CONDITION_AT")){
            UIutil.populateSpinner( this, accValue3, R.array.hours );
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
