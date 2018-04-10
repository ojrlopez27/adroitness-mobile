package edu.cmu.adroitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.ExpandableListAdapter;
import edu.cmu.adroitness.client.commons.control.ViewHelper;
import edu.cmu.adroitness.client.sensors.accelaration.view.AccelerationActivity;
import edu.cmu.adroitness.client.sensors.audio.view.AudioRecordActivity;
import edu.cmu.adroitness.client.sensors.sms.view.SmsSensorActivity;
import edu.cmu.adroitness.client.services.actrecog.view.ActRecognitionActivity;
import edu.cmu.adroitness.client.services.calendar.view.DialogCalendar;
import edu.cmu.adroitness.client.services.gmail.view.ExtendedGmailActivity;
import edu.cmu.adroitness.client.services.googlespeechrecognition.view.ExtendedGoogleSpeechRecognizerActivity;
import edu.cmu.adroitness.client.services.googlespeechrecognition.view.GoogleASR;
import edu.cmu.adroitness.client.services.hotel.view.HotelReservationActivity;
import edu.cmu.adroitness.client.services.location.view.LocationActivity;
import edu.cmu.adroitness.client.services.nell.view.NELLActivity;
import edu.cmu.adroitness.client.services.red5streaming.view.ExtendedRed5StreamingActivity;
import edu.cmu.adroitness.client.services.rules.view.DecisionRuleActivity;
import edu.cmu.adroitness.client.services.weather.view.WeatherActivity;

public class AppMainActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0: //Accelerometer
                                helper.startActivity(AccelerationActivity.class );
                                break;
                            case 1: //SMS
                                helper.startActivity(SmsSensorActivity.class );
                                break;
                            case 2: //Applications

                                break;
                            case 3: //Camera

                                break;
                            case 4: //Audio Record
                                helper.startActivity(AudioRecordActivity.class);
                                break;
                            case 5: //Communication

                                break;
                            case 6: //ESM

                                break;
                            case 7: //Location

                                break;
                            case 8: //MQTT

                                break;
                            case 9: //Processor

                                break;
                            case 10: //Proximity

                                break;
                            case 11: //Rotation

                                break;
                            case 12: //Screen

                                break;
                            case 13: //Battery

                                break;
                            default:

                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0: //AlarmEffector
                                break;
                            case 1: //Sms
                                helper.startActivity(edu.cmu.adroitness.client.effectors.sms.view.SmsEffectorActivity.class);
                                break;
                            case 2: //Phone call
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0: //News
                                break;
                            case 1://Search

                                break;
                            case 2: //Flurry

                                break;
                            case 3: //Weather
                                helper.startActivity(WeatherActivity.class);
                                break;
                            case 4: //Email

                                break;
                            case 5: //Fused Location
                                helper.startActivity(LocationActivity.class);
                                break;
                            case 6: //Activity Recognition
                                helper.startActivity(ActRecognitionActivity.class);
                                break;
                            case 7: //Calendar
                                DialogCalendar dialogCalendar = new DialogCalendar();
                                dialogCalendar.setCancelable(false);
                                dialogCalendar.setHelper( helper );
                                dialogCalendar.show(getFragmentManager(), "caloptions");
                                break;
                            case 8: //Dialogue
                                break;
                            case 9: //Movie Recommendation
                                break;
                            case 10: //Hotel Reservation
                                helper.startActivity(HotelReservationActivity.class);
                                break;
                            case 11: //Red 5 Streaming
                                helper.startActivity(ExtendedRed5StreamingActivity.class);
                                break;
                            case 12: //Streaming
                                break;
                            case 13: //Upload Image
                                helper.uploadImage();
                                break;
                            case 14: //Decision Rules
                                helper.startActivity(DecisionRuleActivity.class);
                                break;
                            case 15: //User Tasks Understanding
                                break;
                            case 16: //OAQA
                                break;
                            case 17: //NEIL
                                break;
                            case 18: //EMAIL
                                break;
                            case 19: //App Tracker
                                break;
                            case 20: //CherishedMemoriesActivity
                                break;
                            case 21: //Multisense
                                break;
                            case 22: //Geotagged Keywords Service
                                break;
                            case 23: //Rapport
                                break;
                            case 24: //Streaming Dialogue
                                break;
                            case 25: //NELL
                                helper.startActivity(NELLActivity.class);
                                break;
                            case 26: //Geotagged Keywords Service
                                break;
                            case 27: //HELPR Service
                                break;
                            case 28: //SUGILITE Service
                                break;
                            case 29: //Chorus Service
                                break;
                            case 30: //Gmail Service
                                helper.startActivity(ExtendedGmailActivity.class);
                                break;
                            case 31: //Google Speech Adapter Service
                                helper.startActivity(ExtendedGoogleSpeechRecognizerActivity.class);
                                break;
                            case 32: //Google Speech Adapter Service
                                helper.startActivity(GoogleASR.class);
                                break;
                            case 33: //Performance test
                                break;
                            default:
                                break;
                        }

                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        // get the helper
        helper = ViewHelper.getInstance( this );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.currentTimeMillis();
        switch (requestCode) {
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {

                }
                break;
        }
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Sensors");
        listDataHeader.add("Effectors");
        listDataHeader.add("Services");

        // Adding child data
        List<String> sensors = new ArrayList<>();
        sensors.add("Accelerometer");
        sensors.add("SMS");
        sensors.add("Applications");
        sensors.add("Camera");
        sensors.add("Audio Record");
        sensors.add("Communication");
        sensors.add("ESM");
        sensors.add("Location");
        sensors.add("MQTT");
        sensors.add("Processor");
        sensors.add("Proximity");
        sensors.add("Rotation");
        sensors.add("Screen");
        sensors.add("Battery");

        List<String> effectors = new ArrayList<>();
        effectors.add("AlarmEffector");
        effectors.add("SSM");
        effectors.add("Phone call");

        List<String> services = new ArrayList<>();
        services.add("Yahoo News");
        services.add("Yahoo Search");
        services.add("Yahoo Flurry");
        services.add("Yahoo Weather");
        services.add("Yahoo Email");
        services.add("Location");
        services.add("Activity Recognition");
        services.add("Calendar");
        services.add("Dialogue Service");
        services.add("Movie Recommendation");
        services.add("Hotel Reservation");
        services.add("Red 5 Streaming");
        services.add("Streaming");
        services.add("Upload Image");
        services.add("Rule Validation");
        services.add("User Tasks Understanding");
        services.add("OAQA");
        services.add("NEIL");
        services.add("Email Understanding");
        services.add("App Tracker");
        services.add("Cherished Memories");
        services.add("Multisense");
        services.add("Geotagged Keywords");
        services.add("Rapport");
        services.add("Streaming Dialogue Service");
        services.add("NELL Service");
        services.add("Geotagged Photo Metadata Survey Service");
        services.add("HELPR");
        services.add("Sugilite");
        services.add("Chorus");
        services.add("Gmail");
        services.add("Google Cloud ASR Adapter");
        services.add("Google Cloud ASR");
        services.add("Performance Test");

        listDataChild.put(listDataHeader.get(0), sensors);
        listDataChild.put(listDataHeader.get(1), effectors);
        listDataChild.put(listDataHeader.get(2), services);
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    protected void onDestroy(){
        super.onDestroy();

        helper.exit();
    }

    @Override
    protected void onPause() {

        //this.moveTaskToBack(true);
        super.onPause();
    }

}
