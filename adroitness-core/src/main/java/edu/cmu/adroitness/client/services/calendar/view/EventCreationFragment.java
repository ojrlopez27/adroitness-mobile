package edu.cmu.adroitness.client.services.calendar.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.calendar.control.CalendarController;
import edu.cmu.adroitness.client.commons.control.UIutil;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscarr on 9/23/15.
 * This fragment will allow you to:
 * 1. INSERT CALENDAR EVENTS
 * 2. UPDATE CALENDAR EVENTS
 */
public class EventCreationFragment extends Fragment {
    protected View fragmentView;
    protected EditText description;
    protected EditText summary;
    protected EditText location;
    protected EditText startDate;
    protected EditText endDate;
    protected EditText startTime;
    protected EditText endTime;

    protected Button addOrUpdate;
    protected Button fill;
    protected CalendarDayDetailActivity calendarDayDetailActivity;
    protected CalendarController calendarController;
    protected CalendarEventVO eventToModify;
    protected boolean debugMode = false;
    protected boolean isEventModified;
    protected static int cont = 0;
    protected static Date dateSelected;
    

    public EventCreationFragment(){}

    @SuppressLint("ValidFragment")
    public EventCreationFragment(CalendarDayDetailActivity activity, CalendarEventVO calendarEventVO,
                                 Boolean isEventModified) {
        this.calendarDayDetailActivity = activity;
        this.eventToModify = calendarEventVO;
        this.isEventModified = isEventModified;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarController = CalendarController.getInstance( );
        calendarController.setEventCreationFragment( this );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.services_calendar_fragment_event_creation, container, false);
        description = (EditText) fragmentView.findViewById(R.id.textDescription);
        summary = (EditText) fragmentView.findViewById(R.id.textSummary);
        location = (EditText) fragmentView.findViewById(R.id.textLocation);
        startDate = (EditText) fragmentView.findViewById(R.id.textStartDate);
        endDate = (EditText) fragmentView.findViewById(R.id.textEndDate);
        startTime = (EditText) fragmentView.findViewById(R.id.textStartTime);
        endTime = (EditText) fragmentView.findViewById(R.id.textEndTime);
        dateSelected = CalendarDayDetailActivity.selectedDate;

        startDate.setText(Util.getDate(dateSelected, "yyyy-MM-dd" ));
        startDate.setTag(dateSelected);
        endDate.setText(Util.getDate(dateSelected, "yyyy-MM-dd" ));
        endDate.setTag(dateSelected);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "DATE", startDate, calendarDayDetailActivity);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "DATE", endDate, calendarDayDetailActivity);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "TIME", startTime, calendarDayDetailActivity);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "TIME", endTime, calendarDayDetailActivity);
            }
        });

        if(isEventModified){
            if( eventToModify != null ) {
                //fill the UI widgets with the event data to be modified
                description.setText(eventToModify.getDescription());
                summary.setText(eventToModify.getSummary());
                location.setText(eventToModify.getLocation());
                startDate.setText(eventToModify.getFormattedStartDate());
                endDate.setText(eventToModify.getFormattedEndDate());
                startTime.setText(eventToModify.getFormattedStartTime());
                endTime.setText(eventToModify.getFormattedEndTime());
                startDate.setTag( eventToModify.getStartDate() );
                endDate.setTag( eventToModify.getEndDate() );

                addOrUpdate = (Button) fragmentView.findViewById(R.id.addEventBtn);
                addOrUpdate.setText("Modify Event");
                addOrUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(calendarDayDetailActivity, "Updating Event",
                                Toast.LENGTH_LONG).show();
                        /** 2. UPDATE CALENDAR EVENTS **/
                        updateCalendarEvent( eventToModify.getId() );
                        isEventModified = false;
                        eventToModify = null;
                    }
                });
            }else{
                Log.e("EventCreationFragment", "Calendar event to be modified is null");
            }
        }else {
            addOrUpdate = (Button) fragmentView.findViewById(R.id.addEventBtn);
            addOrUpdate.setText( "Create Event" );
            addOrUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(calendarDayDetailActivity, "Creating Event", Toast.LENGTH_LONG).show();
                    isEventModified =false;
                    eventToModify = null;
                    /** 1. INSERT CALENDAR EVENTS **/
                    createCalendarEvent( );
                }
            });

            fill = (Button) fragmentView.findViewById(R.id.fillBtn);
            if( debugMode ) {
                fill.setVisibility( View.VISIBLE );
                fill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        description.setText("This is a follow up meeting " + cont);
                        summary.setText("Adroitness Meeting " + cont);
                        location.setText("CMU, Pittsburgh");
                        Calendar cal = Calendar.getInstance();
                        //By default, the event is created for the selected day
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateSelected);
                        //cal.set(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH)+1);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
                        String date = calendar.get(Calendar.YEAR) + "-"
                                //we need the actual month index: Jan = 1, Feb = 2, March = 3...
                                + (calendar.get(Calendar.MONTH) + 1) + "-"
                                + calendar.get(Calendar.DAY_OF_MONTH);
                        startDate.setText(date);
                        startDate.setTag(calendar.getTime());
                        endDate.setText(date);
                        endDate.setTag(calendar.getTime());
                        int randomTime = (int) (Math.random() * 23);
                        startTime.setText(randomTime + ":00");
                        //by default, event will take only one hour
                        endTime.setText((randomTime + 1) + ":00");
                        cont++;
                    }
                });
            }else{
                fill.setVisibility( View.INVISIBLE );
            }
        }
        return fragmentView;
    }


    public EditText getDescription() {
        return description;
    }

    public EditText getSummary() {
        return summary;
    }

    public EditText getLocation() {
        return location;
    }

    public EditText getStartDate() {
        return startDate;
    }

    public EditText getEndDate() {
        return endDate;
    }

    public EditText getStartTime() {
        return startTime;
    }

    public EditText getEndTime() {
        return endTime;
    }

    protected void createCalendarEvent() {
        calendarController.createCalendarEvent( getActivity() );
    }

    protected void updateCalendarEvent( String eventId ){
        calendarController.updateCalendarEvent(getActivity(), eventId);
    }
}