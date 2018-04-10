package edu.cmu.adroitness.client.services.booking.control;

import android.util.JsonReader;

import edu.cmu.adroitness.comm.hotel.model.HotelReservationEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.booking.model.Criteria;
import edu.cmu.adroitness.client.services.booking.model.HotelSearchCriteria;
import edu.cmu.adroitness.client.services.booking.model.HotelVO;
import edu.cmu.adroitness.client.services.generic.control.GenericService;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Callable;


public class HotelReservationService extends GenericService implements Booking {
    private static String URL = "http://api.hotwire.com/v1/search/hotel?apikey=";

    private ArrayList<HotelVO> hotels;
    private HotelSearchCriteria searchCriteria = new HotelSearchCriteria();

    public HotelReservationService(){
        super(null);
        if( actions.isEmpty() ) {
            this.actions.add(Constants.ACTION_HOTEL_RESERVATION);
        }
        hotels = new ArrayList<>();
    }

    @Override
    public ArrayList<HotelVO> search( final Criteria criteria ) {
        return Util.executeSync(new Callable<ArrayList<HotelVO>>() {
            @Override
            public ArrayList<HotelVO> call() {
                searchCriteria = (HotelSearchCriteria) criteria;
                hotels = new ArrayList<>();
                if (checkInputs()) {
                    String urlString = String.format(URL +
                                    "%s&dest=%s" +
                                    "&rooms=%s" +
                                    "&children=%s" +
                                    "&adults=%s" +
                                    "&startdate=%s" +
                                    "&enddate=%s" +
                                    "&limit=%s" +
                                    "&sort=%s" +
                                    "&format=json",
                            UtilServiceAPIs.API_KEY_HOTWIRE,
                            searchCriteria.getAddress().replaceAll("\\s+", "+"),
                            searchCriteria.getRooms(),
                            searchCriteria.getChildren(),
                            searchCriteria.getAdults(),
                            Util.getDate(searchCriteria.getStartDate(), "MM/dd/yyyy"),
                            Util.getDate(searchCriteria.getEndDate(), "MM/dd/yyyy"),
                            searchCriteria.getResultLimit(),
                            searchCriteria.getSortBy());
                    hotels = searchHotelShopping(urlString);
                }
                return hotels;
            }
        });
    }

    @Override
    public boolean checkInputs() {
        return searchCriteria.getStartDate().before( searchCriteria.getEndDate() );
    }

    private ArrayList<HotelVO> searchHotelShopping(final String urlString) {
        try {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                try {
                    parseHotelShopping(reader);
                } finally {
                    reader.close();
                }
            } catch (MalformedURLException e) {
                mb.send(HotelReservationService.this, HotelReservationEvent.build()
                        .setErrorMessage(e.getMessage()));
                e.printStackTrace();
            } catch (Exception e) {
                mb.send(HotelReservationService.this, HotelReservationEvent.build()
                        .setErrorMessage(e.getMessage()));
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotels;
    }


    private void parseHotelShopping(JsonReader reader) throws Exception {
        HashMap<String, String> mappings = new HashMap<>();
        mappings.put("Result", "*");
        mappings.put("Errors", "");
        mappings.put("DeepLink", "setUrl");
        mappings.put("TotalPrice", "setTotalPrice");
        mappings.put("AmenityCodes", "");
        mappings.put("AmenityCodes.childString", "setAmenityCode");
        mappings.put("AmenityCodes.Code", "setAmenityCode");
        mappings.put("AveragePricePerNight", "setPricePerNight");
        mappings.put("RecommendationPercentage", "setRecommendationPercentage");
        mappings.put("SavingPercentage", "setSavingPercentage");
        mappings.put("StarRating", "setStarRating");
        mappings.put("ErrorMessage", "break");
        mappings.put("Error", "");

        ArrayList errors = new ArrayList();
        hotels.clear();
        Util.readJsonToObject(reader, "", mappings, new HotelVO(), hotels, errors);
        if( errors.isEmpty() ) {
            mb.send(HotelReservationService.this, HotelReservationEvent.build().setHotels(hotels));
        } else{
            mb.send(HotelReservationService.this, HotelReservationEvent.build()
                    .setErrorMessage(Arrays.toString(errors.toArray() )));
        }
    }

    @Override
    public void doAfterBind() {

    }

    @Override
    public void onDestroy(){
        this.searchCriteria = null;
        this.hotels = null;
        super.onDestroy();
    }

}
