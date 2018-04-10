package edu.cmu.adroitness.client.sensors.speech.control;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by oscarr on 2/18/15.
 */
public class SpeechController {

    private SpeechRecognizer sr;
    private TextToSpeech tts;
    private Context context;
    private HashMap workingMemory;

    // commands
    private static final String PREVIOUS = "previous";
    private static final String NEXT = "next";
    private static final String EXPAND = "expand";
    private static final String READ = "read";
    private static final String STOP = "stop";
    private static final String RESUME = "resume";
    public static boolean VOICE = true;
    public static boolean STOPPED = false;


    // ****************************** SERVICE'S LIFECYCLE ******************************************

//    public SpeechController( Context context, BNController bnController ) {
//        // you can replace this with MessageBroker.getInstance( getApplicationContext() )
//        // and will produce the same result
//        this.context = context;
////        this.workingMemory = bnController.getWorkingMemory();
////        bnController.setSpeechController( this );
//        this.sr = SpeechRecognizer.createSpeechRecognizer( context );
//        init();
//    }

    public void onDestroy(){
        if( sr != null ){
            sr.destroy();
        }
        if( tts != null ){
            tts.stop();
            tts.shutdown();
        }
    }

    public void onResume() {
        init();
    }


    private void init(){
        sr.setRecognitionListener( new Listener() );
        recognizeSpeech();
        tts = new TextToSpeech( context, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                tts.setLanguage( Locale.US );
            }
        });
    }


    // ****************************** COMMANDS *****************************************************

    public void showArticle(){
        MBRequest.build(Constants.MSG_SHOW_NEWS_ARTICLE)
                .put(Constants.BUNDLE_ARTICLE_ID, 3);
    }

    public void showCurrentArticle(){
        MBRequest.build(Constants.MSG_SHOW_CURRENT_NEWS_ARTICLE);
    }

    public void nextArticle(){
        MBRequest.build(Constants.MSG_SHOW_NEXT_NEWS_ARTICLE);
    }

    public void previousArticle(){
        MBRequest.build(Constants.MSG_SHOW_PREVIOUS_NEWS_ARTICLE);
    }

    public void expandArticle(){
        MBRequest.build(Constants.MSG_EXPAND_NEWS_ARTICLE);
        // or you can expand an arbitrary article index.
        //.put( Constants.BUNDLE_ARTICLE_ID, 5 );
    }

    public void speak( final String message, final String id ){
        ((Activity) context).runOnUiThread( new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                sr.stopListening();
                tts.speak( message, TextToSpeech.QUEUE_FLUSH, null, id );
            }
        });
    }

    // ****************************** SPEECH-TO-TEXT NAD TEXT-TO-SPEECH *****************************


    class Listener implements RecognitionListener {
        public void onReadyForSpeech(Bundle params){
            Log.e("","");
        }
        public void onBeginningOfSpeech(){
            Log.e("","");
        }
        public void onRmsChanged(float rmsdB){
            Log.e("","");
        }
        public void onBufferReceived(byte[] buffer){
            Log.e("","");
        }
        public void onEndOfSpeech(){
            Log.e("","");
        }
        public void onError(int error){
            recognizeSpeech();
        }
        public void onResults(Bundle results){
            ArrayList commands = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Log.e("DEBUG", "commands: " + commands);
            processCommand(commands);
            recognizeSpeech();
        }
        public void onPartialResults(Bundle partialResults){
            Log.e("","");
        }
        public void onEvent(int eventType, Bundle params){
            Log.e("","");
        }
    }

    /**
     * google speech input dialog
     * */
    public void recognizeSpeech() {
        if( VOICE == true ) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            sr.startListening(intent);
        }
    }

    /**
     * Process speech input command
     * */
    public void processCommand( ArrayList<String> commands) {
        for( String command : commands ) {
            if( command.equalsIgnoreCase( STOP ) ){
                STOPPED = true;
                return;
            } else if( command.equalsIgnoreCase( RESUME ) ){
                STOPPED = false;
            } else if (command.equals(NEXT)) {
                nextArticle();
                return;
            } else if (command.equals( PREVIOUS )) {
                previousArticle();
                return;
            } else if (command.equals(EXPAND)) {
                expandArticle();
                return;
            } else if (command.equals(READ)) {
                return;
            } else if(command.contains("plan a trip to") ){
                int pos = command.indexOf("to") + 3;
                if( pos >= command.length() ){
//                    speak("Please, indicate a destination", BNController.NO_DESTINATION);
                }else {
                    String destination = command.substring( pos );
//                    if (destination.isEmpty()) {
//                        speak("Please, indicate a destination", BNController.NO_DESTINATION);
//                    } else {
//                        workingMemory.put(BNController.CHUNK_DESTINATION, destination);
//                        workingMemory.put(BNController.PLAN_TRAVEL_REQUEST, true);
//                        speak("OK", "Confirmation");
//                    }
                }
                return;
            } else if( command.contains("travel dates from") ){
                int pos1 = command.indexOf(" from ") + 6;
                int pos2 = command.indexOf(" to ", pos1 );
                if( pos2 >= command.length() || pos1 == -1 || pos2 == -1 ){
//                    speak("Please, indicate the dates of your travel", BNController.NO_TRAVEL_DATES);
                } else {
                    String startDate = command.substring(pos1, pos2);
                    pos1 = command.indexOf(" to ", pos2) + 4;
                    String endDate = command.substring(pos1);
                    if (startDate.isEmpty() || endDate.isEmpty()) {
//                        speak("Please, indicate the dates of your travel", BNController.NO_TRAVEL_DATES);
                    } else {
                        int startMonth = getMonth(startDate);
                        int startDay = getDay(startDate);
                        int endMonth = getMonth(endDate);
                        int endDay = getDay(endDate);
                        if (startMonth == -1 || startDay == -1 || endMonth == -1 || endDay == -1) {
//                            speak("Please, indicate the dates of your travel", BNController.NO_TRAVEL_DATES);
                        } else {
                            GregorianCalendar sdt = new GregorianCalendar(2015, startMonth, startDay);
                            GregorianCalendar edt = new GregorianCalendar(2015, endMonth, endDay);
                            Date startDateTrip = sdt.getTime();
                            Date endDateTrip = edt.getTime();
//                            workingMemory.put(BNController.CHUNK_START_DATE_TRIP, startDateTrip);
//                            workingMemory.put(BNController.CHUNK_END_DATE_TRIP, endDateTrip);
                            speak("OK", "Confirmation");
                        }
                    }
                }
                return;
            } else if( command.contains( "add event on" ) ){
                String eDate = command.substring( command.indexOf( " on" ) + 4 );
                int eventMonth = getMonth(eDate);
                int eventDay = getDay(eDate);
                if( eventMonth == -1 || eventDay == -1 ){
//                    speak( "Please, indicate the dates of the event", BNController.NO_EVENT_DATE);
                }else {
                    GregorianCalendar calendar = new GregorianCalendar(2015, eventMonth, eventDay );
                    Date eventDate = calendar.getTime();
//                    workingMemory.put(BNController.CHUNK_NEW_EVENT, eventDate);
//                    workingMemory.put(BNController.NO_EVENT_DATE, true);
//                    workingMemory.put(BNController.ADD_EVENT_REQUEST, true);
                    speak( "OK", "Confirmation" );
                }
                return;
            } else if( command.contains(" say again") ){
                return;
            }
        }
        Log.e("DEBUG", "command: " + commands.toString() );
        speak( "Say again", "ERROR" );
    }


    private int getMonth(String message){
        if( message.contains("january") ){
            return 0;
        } else if( message.contains("february") ){
            return 1;
        } else if( message.contains("march") ){
            return 2;
        } else if( message.contains("april") ){
            return 3;
        } else if( message.contains("may") ){
            return 4;
        } else if( message.contains("june") ){
            return 5;
        } else if( message.contains("july") ){
            return 6;
        } else if( message.contains("august") ){
            return 7;
        } else if( message.contains("september") ){
            return 8;
        } else if( message.contains("october") ){
            return 9;
        } else if( message.contains("november") ){
            return 10;
        } else if( message.contains("december") ){
            return 11;
        }
        return -1;
    }

    private int getDay( String message ){
        message = message+"*";
        for( int i = 1; i < 32; i++  ){
            if( message.contains(" "+i+"st") || message.contains(" "+i+"nd") ||
                    message.contains(" "+i+"rd") || message.contains(" "+i+"th") || message.contains(" "+i+"*")){
                return i;
            }
        }
        return -1;
    }


}
