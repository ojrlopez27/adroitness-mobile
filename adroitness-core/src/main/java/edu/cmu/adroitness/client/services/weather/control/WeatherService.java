package edu.cmu.adroitness.client.services.weather.control;

import android.util.JsonReader;

import edu.cmu.adroitness.comm.weather.model.WeatherEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.UtilServiceAPIs;
import edu.cmu.adroitness.client.services.generic.control.GenericService;
import edu.cmu.adroitness.client.services.location.control.LocationService;
import edu.cmu.adroitness.client.services.location.model.LocationVO;
import edu.cmu.adroitness.client.services.weather.model.DayWeatherVO;
import edu.cmu.adroitness.client.services.weather.model.HourWeatherVO;
import edu.cmu.adroitness.client.services.weather.model.WeatherVO;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherService extends GenericService {

    private ConcurrentHashMap<String, ForecastReport> forecastReports;
    private ConcurrentHashMap<String, WeatherProposition> propositions;

    public WeatherService() {
        super(null);
        if( actions.isEmpty() ) {
            this.actions.add(Constants.ACTION_WEATHER);
        }
        forecastReports = new ConcurrentHashMap<>();
        propositions = new ConcurrentHashMap<>();
    }

    @Override
    public void doAfterBind() {
    }

    public void findWeatherData(final LocationVO locationVO, final int forecastMode,
                                final long refreshTime, final boolean forceRefresh,
                                final List<WeatherProposition> rules) {
        Util.executeSync(new Callable<Void>() {
            @Override
            public Void call() throws Exception{
                try {
                    LocationVO locationTemp = locationVO;
                    locationTemp = getPlaceLocation(locationTemp, true);
                    if (locationTemp != null) {
                        ForecastReport forecastReport = getForecastReport(locationTemp, refreshTime, rules);
                        if (forecastReport != null) {
                            forecastReport.forecastMode = forecastMode;
                            if (forceRefresh) {
                                forecastReport.hourlyWeatherList.clear();
                                forecastReport.dailyWeatherList.clear();
                            }
                            queryDailyWeather(forceRefresh, forecastReport);
                            queryHourlyWeather(forceRefresh, forecastReport);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void queryDailyWeather( final boolean forceRefresh, final ForecastReport forecastReport) {
        Util.executeSync(new Callable<Void>() {
            @Override
            public Void call() throws Exception{
                try {
                    if (forceRefresh || forecastReport.dailyWeatherList.isEmpty()) {
                        findDailyWeather(forecastReport);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void queryHourlyWeather(final boolean forceRefresh, final ForecastReport forecastReport) {
        Util.executeSync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    if (forceRefresh || forecastReport.hourlyWeatherList.isEmpty()) {
                        findHourlyWeather(forecastReport);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }


    private void findDailyWeather(final ForecastReport forecastReport) {
        Util.executeSync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(UtilServiceAPIs.API_FORECAST_YQL_URL.replace("replace_place",
                            URLEncoder.encode(forecastReport.place, "UTF-8")));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                    parseDailyWeather(reader, forecastReport);
                    reader.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return null;
            }
        });
    }


    private void findHourlyWeather(final ForecastReport forecastReport) {
        Util.executeSync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HttpURLConnection urlConnection = null;
                try {
                    LocationVO locationVO = forecastReport.locationVO;
                    URL url = new URL(String.format(UtilServiceAPIs.API_WUNDERGROUND,
                            locationVO.getCountryCode().equals("US") ? locationVO.getStateCode()
                                    : locationVO.getCountryCode(), locationVO.getPlace()));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                    parseHourlyWeather(reader, forecastReport);
                    reader.close();
                    in.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return null;
            }
        });
    }


    private synchronized ForecastReport getForecastReport(String place, long refreshTime,
                                                          List<WeatherProposition> rules) {
        return getForecastReport( new LocationVO( place ), refreshTime, rules );
    }


    private synchronized ForecastReport getForecastReport(final LocationVO location, final long refreshTime,
                                                          final List<WeatherProposition> rules) {
        return Util.executeSync(new Callable<ForecastReport>() {
            @Override
            public ForecastReport call() throws Exception {
                LocationVO locationVO = location;
                String place = locationVO.getSubArea() != null ? locationVO.getSubArea()
                        : locationVO.getCity() != null ? locationVO.getCity()
                        : locationVO.getWoeidVO() != null && locationVO.getWoeidVO().getName() != null ?
                        locationVO.getWoeidVO().getName() : null;
                if (place == null) {
                    mb.send(WeatherService.this,
                            WeatherEvent.build().setIsError(true)
                                    .setErrorMessage("A place is required in order to get the forecast report."));
                    return null;
                }
                ForecastReport forecastReport = forecastReports.get(place.replace(" ", "_"));
                if (forecastReport == null) {
                    forecastReport = new ForecastReport(refreshTime);
                    forecastReport.place = place;
                    forecastReports.put(place, forecastReport);
                }
                if (locationVO == null) {
                    locationVO = new LocationVO(place);
                }
                if (forecastReport.locationVO != null) {
                    forecastReport.locationVO.mergeLocation(locationVO);
                } else {
                    forecastReport.locationVO = locationVO;
                }

                if (refreshTime > 0 && refreshTime != forecastReport.refreshTime) {
                    forecastReport.refreshTime = refreshTime;
                    forecastReport.initTimer();
                }
                if (rules != null && !rules.isEmpty()) {
                    forecastReport.rules = rules;
                }
                return forecastReport;
            }
        });
    }


    private void parseDailyWeather(final JsonReader reader, final ForecastReport forecastReport) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> mappings = new HashMap<>();
                mappings.put("low", "setLowTemp");
                mappings.put("high", "setHighTemp");
                mappings.put("text", "setCondition");
                mappings.put("date", "setDate");
                mappings.put("day", "setDayOfWeek");
                mappings.put("query", "");
                mappings.put("results", "");
                mappings.put("channel", "");
                mappings.put("item", "");
                mappings.put("forecast", "*");
                reader.setLenient(true);
                ArrayList errors = new ArrayList();
                Util.readJsonToObject(reader, "", mappings, new DayWeatherVO(), forecastReport
                        .dailyWeatherList, errors);
                sendResponse( forecastReport, errors, true);
            }
        });
    }

    private void parseHourlyWeather(final JsonReader reader, final ForecastReport forecastReport)
            throws Exception {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> mappings = new HashMap<>();
                mappings.put("hourly_forecast", "*");
                mappings.put("FCTTIME", "");
                mappings.put("hour_padded", "setHour");
                mappings.put("min", "setMinute");
                mappings.put("year", "setYear");
                mappings.put("mon_padded", "setMonth");
                mappings.put("mday_padded", "setDay");
                mappings.put("temp", "");
                mappings.put("temp.english", "setHighTempEnglish");
                mappings.put("temp.metric", "setHighTempMetric");
                mappings.put("dewpoint", "");
                mappings.put("dewpoint.english", "setLowTempEnglish");
                mappings.put("dewpoint.metric", "setLowTempMetric");
                mappings.put("feelslike", "");
                mappings.put("feelslike.english", "setFeelslikeTempEnglish");
                mappings.put("feelslike.metric", "setFeelslikeTempMetric");
                mappings.put("snow", "");
                mappings.put("snow.english", "setSnowEnglish");
                mappings.put("snow.metric", "setSnowMetric");
                mappings.put("condition", "setCondition");
                mappings.put("humidity", "setHumidity");
                mappings.put("icon_url", "setIconUrl");
                mappings.put("response", "");
                mappings.put("error", "");
                mappings.put("error.description", "break");
                ArrayList errors = new ArrayList();
                Util.readJsonToObject(reader, "", mappings, new HourWeatherVO(), forecastReport
                        .hourlyWeatherList, errors);
                sendResponse( forecastReport, errors, true);
            }
        });
    }

    /**
     *
     * @param forecastReport
     * @param errors
     * @param updateMap when sending partial results to the user (e.g., results after applying rules)
     *                  we do not want to update the HashMap with partial results and remove whole results
     */
    private synchronized void sendResponse(final ForecastReport forecastReport, final ArrayList errors,
                                           final boolean updateMap){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                if( errors == null || errors.isEmpty() ){
                    //forecast mode has to be either DAILY or HOURLY, otherwise both lists (daily and hourly)
                    //must be filled before sending the result
                    if( (forecastReport.forecastMode >= 0 || ( forecastReport.forecastMode < 0
                            && !forecastReport.hourlyWeatherList.isEmpty() &&
                            !forecastReport.dailyWeatherList.isEmpty()) )) {
                        if (updateMap) {
                            forecastReports.remove(forecastReport.place);
                            forecastReports.put(forecastReport.place, forecastReport);
                        }
                        fillPlace( forecastReport );
                        WeatherEvent event = WeatherEvent.build()
                                .setPlace(forecastReport.place)
                                .setForecastMode(forecastReport.forecastMode);
                        if (forecastReport.forecastMode == Constants.WEATHER_FORECAST_DAILY) {
                            event.setDailyWeather(forecastReport.dailyWeatherList);
                        } else if (forecastReport.forecastMode == Constants.WEATHER_FORECAST_HOURLY) {
                            event.setHourlyWeather(forecastReport.hourlyWeatherList);
                        } else {
                            event.setDailyWeather(forecastReport.dailyWeatherList);
                            event.setHourlyWeather(forecastReport.hourlyWeatherList);
                        }
                        mb.send(WeatherService.this, event);
                    }
                }else{
                    mb.send( WeatherService.this,
                            WeatherEvent.build()
                                    .setIsError(true)
                                    .setErrorMessage( Arrays.toString( errors.toArray()) ));
                }
            }
        });
    }

    private void fillPlace( ForecastReport forecastReport ) {
        for( HourWeatherVO hourWeatherVO : forecastReport.hourlyWeatherList ){
            hourWeatherVO.setPlace( forecastReport.place );
        }
        for( DayWeatherVO dayWeatherVO : forecastReport.dailyWeatherList ){
            dayWeatherVO.setPlace( forecastReport.place );
        }
    }

    private List validateRules(final ForecastReport forecastReport) {
        return Util.executeSync(new Callable<List>() {
            @Override
            public List call() throws Exception {
                List results = new ArrayList();
                if( forecastReport.rules != null && !forecastReport.rules.isEmpty() ){
                    ArrayList<WeatherProposition> tempList = new ArrayList(forecastReport.rules);
                    for( WeatherProposition wp : propositions.values() ){
                        if( !tempList.contains( wp ) ) tempList.add( wp );
                    }
                    for(WeatherProposition weatherProposition : tempList) {
                        results.addAll( weatherProposition.validate( forecastReport.hourlyWeatherList,
                                forecastReport.dailyWeatherList) );
                    }
                }
                return results;
            }
        });
    }

    public int changeForecastMode(final String place){
        return Util.executeSync(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if( place != null && !place.equals("") ){
                    ForecastReport forecastReport = getForecastReport( place , -1, null);
                    if( forecastReport != null ){
                        if(forecastReport.forecastMode == Constants.WEATHER_FORECAST_DAILY){
                            forecastReport.forecastMode = Constants.WEATHER_FORECAST_HOURLY;
                        }else{
                            forecastReport.forecastMode = Constants.WEATHER_FORECAST_DAILY;
                        }
                        return forecastReport.forecastMode;
                    }
                }
                return Constants.WEATHER_FORECAST_HOURLY;
            }
        });
    }

    public List<HourWeatherVO> getHourlyReport(final String place){
        return Util.executeSync(new Callable<List<HourWeatherVO>>() {
            @Override
            public List<HourWeatherVO> call() throws Exception {
                if( place != null && place.equals( getCurrentLocation() ) ){
                    LocationVO locationVO = getPlaceLocation( place, false );
                    return locationVO == null ? null : Util.clone( getForecastReport(locationVO, -1, null).hourlyWeatherList);
                }else {
                    ForecastReport forecastReport = getForecastReport( place, -1, null);
                    return forecastReport == null? null : Util.clone( forecastReport.hourlyWeatherList );
                }
            }
        });
    }

    public List<DayWeatherVO> getDailyReport(final String place){
        return Util.executeSync(new Callable<List<DayWeatherVO>>() {
            @Override
            public List<DayWeatherVO> call() throws Exception {
                if( place != null && place.equals( getCurrentLocation() ) ){
                    LocationVO locationVO = getPlaceLocation( place, false );
                    return locationVO == null ? null : Util.clone(getForecastReport(locationVO, -1, null).dailyWeatherList);
                }else {
                    ForecastReport forecastReport = getForecastReport( place, -1, null);
                    return forecastReport == null? null : Util.clone(forecastReport.dailyWeatherList);
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        if( forecastReports != null ) {
            for (ForecastReport forecastReport : forecastReports.values()) {
                forecastReport.onDestroy();
            }
            this.forecastReports.clear();
            this.forecastReports = null;
        }
        super.onDestroy();
    }


    private void update( final ForecastReport forecastReport ) throws Exception{
        Util.execute(new Runnable() {
            @Override
            public void run() {
                findWeatherData(new LocationVO(forecastReport.place), -1, -1, true, null);
                List results = validateRules( forecastReport );
                if( !results.isEmpty() ){
                    ForecastReport fr = new ForecastReport();
                    fr.place = forecastReport.place;
                    for( Object result : results ){
                        if( result instanceof HourWeatherVO ){
                            fr.hourlyWeatherList.add( (HourWeatherVO) result );
                        }
                        else if( result instanceof DayWeatherVO ){
                            fr.dailyWeatherList.add( (DayWeatherVO) result );
                        }
                    }
                    forecastReport.forecastMode = -1;
                    sendResponse( fr, null, false);
                }
            }
        });
    }

    public synchronized void unsubscribe(final String place) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                String placeCopy = place;
                if( placeCopy != null && !placeCopy.isEmpty() ){
                    if( placeCopy.equals( getCurrentLocation() )) {
                        placeCopy = getPlaceLocation( placeCopy, false).getPlace();
                    }
                    ForecastReport forecastReport = forecastReports.get( placeCopy );
                    if( forecastReport != null ) {
                        forecastReport.onDestroy();
                        forecastReports.remove(forecastReport.place);
                    }
                }
            }
        });
    }

    private LocationVO getPlaceLocation( String place, boolean checkWoeid){
        return resourceLocator.lookupService( LocationService.class)
                .getPlaceLocation(place, checkWoeid);
    }

    private LocationVO getPlaceLocation( LocationVO locationVO, boolean checkWoeid){
        return resourceLocator.lookupService( LocationService.class)
                .getPlaceLocation( locationVO, checkWoeid);
    }

    public HourWeatherVO getCurrentWeather() {
        return Util.executeSync(new Callable<HourWeatherVO>() {
            @Override
            public HourWeatherVO call() throws Exception {
                List<HourWeatherVO> hourReport = forecastReports.get( getCurrentLocation())
                        .hourlyWeatherList;
                if( hourReport != null && !hourReport.isEmpty() ){
                    return hourReport.get( hourReport.size() - 1 );
                }
                return null;
            }
        });

    }

    private String getCurrentLocation(){
        return resourceLocator.lookupService( LocationService.class ).getPlaceLocation(
                Constants.LOCATION_CURRENT_PLACE, false ).getPlace();
    }

    public void addProposition(WeatherProposition weatherProposition) {
        propositions.put( weatherProposition.getUuid(), weatherProposition);
    }

    public void removeProposition(WeatherProposition weatherProposition) {
        propositions.remove( weatherProposition.getUuid() );
    }

    public Object getWeatherObjects() {
        List<WeatherVO> weatherVOs = new ArrayList<>();
        for( ForecastReport forecastReport : forecastReports.values() ){
            weatherVOs.addAll( forecastReport.hourlyWeatherList );
            weatherVOs.addAll( forecastReport.dailyWeatherList );
        }
        return weatherVOs;
    }


    public class ForecastReport {
        private int forecastMode = -1;
        private String place;
        private List<HourWeatherVO> hourlyWeatherList = new ArrayList<>();
        private List<DayWeatherVO> dailyWeatherList = new ArrayList<>();
        private long refreshTime = 60 * 60 * 1000; //in miliseconds -> default: 1 hour
        private List<WeatherProposition> rules;
        private Timer timer;
        private LocationVO locationVO;

        public ForecastReport( long refreshTime ) {
            if( refreshTime > 0 ) {
                this.refreshTime = refreshTime;
                initTimer();
            }
        }

        public ForecastReport(){} 

        public void onDestroy(){
            hourlyWeatherList = null;
            dailyWeatherList = null;
            rules = null;
            resetTimer();
            locationVO = null;
        }

        private void resetTimer(){
            if( timer != null ) {
                timer.purge();
                timer.cancel();
                timer = null;
            }
        }

        public void initTimer(){
            resetTimer();
            timer = new Timer();
            timer.schedule( new UpdateTimerTask(), refreshTime, refreshTime );
        }

        private final class UpdateTimerTask extends TimerTask {
            @Override
            public void run() {
                try {
                    update( ForecastReport.this );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
