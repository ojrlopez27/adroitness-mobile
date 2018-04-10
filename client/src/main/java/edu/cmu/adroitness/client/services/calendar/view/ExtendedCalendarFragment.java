package edu.cmu.adroitness.client.services.calendar.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yahoo.inmind.services.calendar.model.CalendarEventVO;
import com.yahoo.inmind.services.calendar.view.EventCreationFragment;

/**
 * Created by oscarr on 4/13/16.
 * This fragment will allow you to:
 * 1. INSERT CALENDAR EVENTS
 * 2. UPDATE CALENDAR EVENTS
 */
public class ExtendedCalendarFragment extends EventCreationFragment {
    private final String TAG = ExtendedCalendarFragment.class.getName();

    public ExtendedCalendarFragment(){
        super();
    }

    @SuppressLint("ValidFragment")
    public ExtendedCalendarFragment(ExtendedCalendarDayDetailActivity activity,
                                    CalendarEventVO calendarEventVO, Boolean isEventModified) {
        this.calendarDayDetailActivity = activity;
        this.eventToModify = calendarEventVO;
        this.isEventModified = isEventModified;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if( !isEventModified){
            addOrUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(calendarDayDetailActivity, "Creating Calendar Event",
                            Toast.LENGTH_LONG).show();
                    isEventModified = false;
                    eventToModify = null;
                    /** 1. INSERT CALENDAR EVENTS **/
                    createCalendarEvent( );
                }
            });
        }else{
            if( eventToModify != null ) {
                addOrUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(calendarDayDetailActivity, "Updating Calendar Event",
                                Toast.LENGTH_LONG).show();
                        /** 2. UPDATE CALENDAR EVENTS **/
                        updateCalendarEvent( eventToModify.getId() );
                        isEventModified = false;
                        eventToModify = null;
                    }
                });
            }else{
                Log.e(TAG, "Calendar event to be modified is null");
            }
        }
        return fragmentView;
    }
}
