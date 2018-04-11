package edu.cmu.adroitness.client.services.hotel.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.adroitness.client.R;
import edu.cmu.adroitness.client.commons.control.UIutil;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.booking.model.HotelSearchCriteria;
import edu.cmu.adroitness.client.services.booking.model.HotelVO;
import edu.cmu.adroitness.client.services.hotel.control.HotelAdapter;
import edu.cmu.adroitness.client.services.hotel.control.ViewHelper;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.hotel.model.HotelReservationEvent;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HotelReservationActivity extends AppCompatActivity {
    private HotelSearchCriteria searchCriteria;

    private static TextView address;
    private static TextView rooms;
    private static TextView children;
    private static TextView adults;
    private static EditText startDate;
    private static EditText endDate;
    private ListView searchResults;
    private HotelAdapter searchResultsAdapter;

    private ArrayList<HotelVO> hotels;
    private ViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_hotel_activity);
        address = (TextView) findViewById(R.id.address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText( v );
            }
        });
        adults = (TextView) findViewById(R.id.adults);
        adults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText( v );
            }
        });
        children = (TextView) findViewById(R.id.children);
        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText( v );
            }
        });
        rooms = (TextView) findViewById(R.id.rooms);
        rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText(v);
            }
        });
        startDate = (EditText) findViewById(R.id.textStartDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "DATE", startDate, HotelReservationActivity.this);
            }
        });
        endDate = (EditText) findViewById(R.id.textEndDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIutil.showPickerDialog(v, "DATE", endDate, HotelReservationActivity.this);
            }
        });
        searchResults = (ListView) findViewById(R.id.hotelResults);
        hotels = new ArrayList<>();
        searchResultsAdapter = new HotelAdapter(this, R.layout.services_hotel_adapter, hotels);
        searchResults.setAdapter(searchResultsAdapter);

        //initialize communication with Message Broker (Middleware)
        MessageBroker.getInstance(getApplicationContext()).subscribe( this );
        helper = ViewHelper.getInstance( this );
    }

    public void fillFast(View v) {
        address.setText("Destination: New York");
        adults.setText("Adults: 1");
        rooms.setText("Rooms: 1");
        children.setText("Children: 0");

        Calendar cal= Calendar.getInstance();
        // reservation is done 20 days in advance
        cal.add(Calendar.DAY_OF_MONTH, 20);
        String date = Util.getDate( cal.getTime(), "yyyy-MM-dd" );
        startDate.setText("Start: " + date);
        startDate.setTag(cal.getTime());
        // reservation will last 7 days
        cal.add( Calendar.DAY_OF_MONTH, 7);
        date = Util.getDate( cal.getTime(), "yyyy-MM-dd" );
        endDate.setText( "End: " + date );
        endDate.setTag( cal.getTime() );
    }


    public void search(View v) {
        searchCriteria = new HotelSearchCriteria();
        searchCriteria.setAddress( address.getText().toString().startsWith("Destination:")?
                address.getText().toString().substring(13) : address.getText().toString());
        searchCriteria.setAdults( adults.getText().toString().startsWith("Adults:")?
                adults.getText().toString().substring(8) : adults.getText().toString());
        searchCriteria.setRooms( rooms.getText().toString().startsWith("Rooms:")?
                rooms.getText().toString().substring(7) : rooms.getText().toString() );
        searchCriteria.setChildren( children.getText().toString().startsWith("Children:")?
                children.getText().toString().substring(10) : children.getText().toString());
        searchCriteria.setStartDate( (Date) startDate.getTag() );
        searchCriteria.setEndDate( (Date) endDate.getTag());
        searchCriteria.setResultLimit(10);
        searchCriteria.setSortBy( HotelSearchCriteria.SORT_BY_PRICE);
        if( checkInputs(searchCriteria.getStartDate(), searchCriteria.getEndDate()) ){
            hotels.clear();
            hotels.addAll( helper.searchHotel(searchCriteria) );
        }else{
            Toast.makeText(this, "Dates are not correct", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * EVENT HANDLER.
     * Here you will receive the results for the hotels search. You must implement this method!
     * @param event
     */
    public void onEvent( final HotelReservationEvent event ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( event.getErrorMessage() != null && !event.getErrorMessage().equals("") ){
                    Toast.makeText(HotelReservationActivity.this, "Error: " + event.getErrorMessage(),
                            Toast.LENGTH_LONG).show();
                }else {
                    searchResultsAdapter.clear();
                    searchResultsAdapter.addAll(event.getHotels());
                    searchResultsAdapter.notifyDataSetChanged();
                }
            }
        });
    }





    //TODO: validateRule dates are in the future and startDate > endDate
    public boolean checkInputs(Date start, Date end){
        return start.before( end );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel_reservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void clearText(View v){
        address.setText("");
        adults.setText("");
        children.setText("");
        rooms.setText("");
    }

}
