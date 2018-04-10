package edu.cmu.adroitness.client.services.activity.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aware.utils.Converters;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.activity.control.ActivityRecognitionStats;
import edu.cmu.adroitness.client.services.generic.view.ServiceContextCard;

import java.util.Calendar;

/**
 * New Stream UI cards<br/>
 * Implement here what you see on your Plugin's UI.
 * @author denzilferreira
 */
public class ARContextCard extends ServiceContextCard {

    //Declare here all the UI elements you'll be accessing
    private TextView still, walking, running, biking, driving;

    public ARContextCard(){
        uiChanger = new Runnable() {
            @Override
            public void run() {
                //Modify card's content here once it's initialized
                if( card != null ) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.setTimeInMillis(System.currentTimeMillis());

                    //Modify time to be at the begining of today
                    mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    mCalendar.set(Calendar.MINUTE, 0);
                    mCalendar.set(Calendar.SECOND, 0);
                    mCalendar.set(Calendar.MILLISECOND, 0);

                    //Get stats for today
                    still.setText(Converters.readable_elapsed(ActivityRecognitionStats.getTimeStill(sContext.getContentResolver(), mCalendar.getTimeInMillis(), System.currentTimeMillis())));
                    walking.setText(Converters.readable_elapsed(ActivityRecognitionStats.getTimeWalking(sContext.getContentResolver(), mCalendar.getTimeInMillis(), System.currentTimeMillis())));
                    running.setText(Converters.readable_elapsed(ActivityRecognitionStats.getTimeRunning(sContext.getContentResolver(), mCalendar.getTimeInMillis(), System.currentTimeMillis())));
                    biking.setText(Converters.readable_elapsed(ActivityRecognitionStats.getTimeBiking(sContext.getContentResolver(), mCalendar.getTimeInMillis(), System.currentTimeMillis())));
                    driving.setText(Converters.readable_elapsed(ActivityRecognitionStats.getTimeVehicle(sContext.getContentResolver(), mCalendar.getTimeInMillis(), System.currentTimeMillis())));
                }

                //Reset timer and schedule the next card refresh
                uiRefresher.postDelayed(uiChanger, refresh_interval);
            }
        };
    }



	public View getContextCard(Context context) {
        super.getContextCard(context);

        //Load card information to memory
        card = inflater.inflate(R.layout.ar_layout, null);

        //Initialize UI elements from the card
        still = (TextView) card.findViewById(R.id.time_still);
        walking = (TextView) card.findViewById(R.id.time_walking);
        biking = (TextView) card.findViewById(R.id.time_biking);
        running = (TextView) card.findViewById(R.id.time_running);
        driving = (TextView) card.findViewById(R.id.time_vehicle);

        //Return the card to AWARE/apps
        return card;
	}
}
