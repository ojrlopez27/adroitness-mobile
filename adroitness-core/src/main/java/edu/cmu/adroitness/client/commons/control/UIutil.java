package edu.cmu.adroitness.client.commons.control;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscarr on 8/31/15.
 */
public class UIutil {

    public static void showPickerDialog(View v, String type, EditText editText, AppCompatActivity activity){
        if( type.equals("DATE") ) {
            DialogFragment newFragment = new DatePickerFragment();
            ((DatePickerFragment) newFragment).setText(editText);
            newFragment.show(activity.getSupportFragmentManager(), "datePicker");
        }else  if( type.equals("TIME") ) {
            DialogFragment newFragment = new TimePickerFragment();
            ((TimePickerFragment) newFragment).setText( editText );
            newFragment.show( activity.getSupportFragmentManager(), "timePicker");
        }
    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText text;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if( text != null ){
                text.setTag( Util.getDate(year, month, day) );
                text.setText(Util.getDate((Date) text.getTag(), "yyyy-MM-dd"));
            }
        }

        public EditText getText() {
            return text;
        }

        public void setText(EditText text) {
            this.text = text;
        }
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private EditText text;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            text.setText( hourOfDay + ":" + minute );
        }

        public EditText getText() {
            return text;
        }

        public void setText(EditText text) {
            this.text = text;
        }
    }


    public static void populateSpinner(Context context, Spinner spinner, int arrayLayout){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                arrayLayout, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        if( context instanceof AdapterView.OnItemSelectedListener ) {
            spinner.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) context );
        }
    }
}
