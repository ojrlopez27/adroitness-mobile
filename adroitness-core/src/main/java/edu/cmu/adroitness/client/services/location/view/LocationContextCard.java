package edu.cmu.adroitness.client.services.location.view;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.TextView;

import com.aware.providers.Locations_Provider;
import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.services.generic.view.ServiceContextCard;

import java.io.IOException;
import java.util.List;


public class LocationContextCard extends ServiceContextCard {
    private TextView address;

    public LocationContextCard(){
        uiChanger = new Runnable() {
        @Override
        public void run() {
            //Modify location_card's content here once it's initialized
            if( card != null ) {

                Cursor last_location = sContext.getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
                if( last_location != null && last_location.moveToFirst() ) {
                    double lat = last_location.getDouble(last_location.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
                    double lon = last_location.getDouble(last_location.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));

                    try {
                        Geocoder geo = new Geocoder(sContext);
                        String geo_text = "";
                        List<Address> addressList = geo.getFromLocation(lat, lon, 1);
                        for(int i = 0; i<addressList.size(); i++ ) {
                            Address address1 = addressList.get(i);
                            for( int j = 0; j< address1.getMaxAddressLineIndex(); j++ ) {
                                if( address1.getAddressLine(j).length() > 0 ) {
                                    geo_text += address1.getAddressLine(j) + "\n";
                                }
                            }
                            geo_text+=address1.getCountryName();
                        }
                        address.setText(geo_text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if( last_location != null && ! last_location.isClosed() ) last_location.close();

            }

            //Reset timer and schedule the next location_card refresh
            uiRefresher.postDelayed(uiChanger, refresh_interval);
        }
    };}



    @Override
    public View getContextCard(Context context) {
        super.getContextCard(context);
        card = inflater.inflate(R.layout.location_card, null);
        address = (TextView) card.findViewById(R.id.address);
        return card;
    }
}
