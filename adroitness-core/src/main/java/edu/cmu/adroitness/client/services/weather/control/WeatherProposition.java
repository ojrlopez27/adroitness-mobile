package edu.cmu.adroitness.client.services.weather.control;

import edu.cmu.adroitness.comm.weather.model.WeatherEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.services.generic.model.DataObject;
import edu.cmu.adroitness.client.services.weather.model.DayWeatherVO;
import edu.cmu.adroitness.client.services.location.control.LocationService;
import edu.cmu.adroitness.client.services.weather.model.HourWeatherVO;
import edu.cmu.adroitness.client.services.weather.model.WeatherVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 9/15/15.
 */
public class WeatherProposition extends PropositionalStatement {

    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int hour = -1;
    private int minute = -1;
    private String place;

    public WeatherProposition(){
        super();
        initialize(true);
    }

    public WeatherProposition(String attribute, String operator, String value) {
        super(attribute, operator, value);
        initialize(true);
    }

    public WeatherProposition(boolean subscribe){
        super();
        initialize(subscribe);
    }

    public void initialize(boolean subscribe){
        super.initialize( subscribe );
        place = Constants.LOCATION_CURRENT_PLACE;
        componentName = Constants.WEATHER;
    }

    /**
     * @param attribute
     * @param operator
     * @param value
     * @param year
     * @param month Jan = 1, Feb = 2, ... Dec = 12
     * @param day
     */
    public WeatherProposition(String attribute, String operator, String value, int year, int month,
                              int day) {
        this(attribute, operator, value);
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     *
     * @param attribute
     * @param operator
     * @param value
     * @param year
     * @param month Jan = 1, Feb =2, ... Dec = 12
     * @param day
     * @param hour
     * @param minute
     */
    public WeatherProposition(String attribute, String operator, String value, int year, int month,
                              int day, int hour, int minute) {
        this( attribute, operator, value, year, month, day );
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public void postCreate() {
        resourceLocator.lookupService( WeatherService.class ).addProposition( this );
    }

    public void setPlace(String place) {
        if( place.equals(Constants.LOCATION_CURRENT_PLACE) ){
            place = resourceLocator.lookupService( LocationService.class).getPlaceLocation(
                    place, false).getPlace();
        }
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    /**
     *
     * @param month Jan = 1, Feb = 2 ... Dec = 12
     */
    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    public List validate(List<HourWeatherVO> hourWeatherVOList, List<DayWeatherVO> dayWeatherVOList){
        List results = new ArrayList();
        for (HourWeatherVO hourWeatherVO : hourWeatherVOList) {
            if ((Boolean) validate(hourWeatherVO) && !results.contains(hourWeatherVO)) {
                results.add(hourWeatherVO);
            }
        }
        if( results.isEmpty() ) {
            for (DayWeatherVO dayWeatherVO : dayWeatherVOList) {
                if ((Boolean) validate(dayWeatherVO) && !results.contains(dayWeatherVO)) {
                    results.add(dayWeatherVO);
                }
            }
        }
        return results;
    }

    @Override
    public Object validate(Object objValue) {
        if( objValue == null || !getPlace().equals( ((WeatherVO)objValue).getPlace() )
                || !areDatesEqual(objValue) )
            return false;
        if( objValue instanceof HourWeatherVO){
            HourWeatherVO hourWeatherVO = (HourWeatherVO) objValue;
            if( attribute.equals( Constants.WEATHER_LOW_TEMP_ENG )
                    || attribute.equals( Constants.WEATHER_LOW_TEMP_METRIC )
                    || attribute.equals( Constants.WEATHER_HIGH_TEMP_ENG )
                    || attribute.equals( Constants.WEATHER_HIGH_TEMP_METRIC )
                    || attribute.equals( Constants.WEATHER_FEELS_LIKE_ENG )
                    || attribute.equals( Constants.WEATHER_FEELS_LIKE_METRIC )
                    || attribute.equals( Constants.WEATHER_DAY )
                    || attribute.equals( Constants.WEATHER_MONTH )
                    || attribute.equals( Constants.WEATHER_YEAR )
                    || attribute.equals( Constants.WEATHER_HOUR )
                    || attribute.equals( Constants.WEATHER_HUMIDITY )
                    || attribute.equals( Constants.WEATHER_SNOW_ENG )
                    || attribute.equals( Constants.WEATHER_SNOW_METRIC )){
                return validateNumbers( (Float) convertAttribute( this.attribute, hourWeatherVO),
                        Float.valueOf( this.value ) );
            }
            if( attribute.equals( Constants.WEATHER_CONDITION) ){
                return validateStrings( (String) convertAttribute( attribute, hourWeatherVO) );
            }
        }else if( objValue instanceof DayWeatherVO){
            DayWeatherVO dayWeatherVO = (DayWeatherVO) objValue;
            if( attribute.equals( Constants.WEATHER_LOW_TEMP_ENG )
                    || attribute.equals( Constants.WEATHER_HIGH_TEMP_ENG )
                    || attribute.equals( Constants.WEATHER_DAY )
                    || attribute.equals( Constants.WEATHER_DAY_OF_WEEK )
                    || attribute.equals( Constants.WEATHER_MONTH )
                    || attribute.equals( Constants.WEATHER_YEAR ) ){
                return validateNumbers( (Float) convertAttribute( this.attribute, dayWeatherVO),
                        Float.valueOf( this.value ) );
            }
            if( attribute.equals( Constants.WEATHER_CONDITION) ){
                return validateStrings( (String) convertAttribute( attribute, dayWeatherVO) );
            }
        }
        return false;
    }

    @Override
    public ArrayList validate() {
        return getList(ResourceLocator.getExistingInstance().lookupService(WeatherService.class)
                .getWeatherObjects());
    }

    public Object convertAttribute( String attribute, HourWeatherVO hourWeatherVO ){
        if( attribute.equals( Constants.WEATHER_CONDITION ) ){
            return hourWeatherVO.getCondition() != null? hourWeatherVO.getCondition() : "";
        }
        if( attribute.equals( Constants.WEATHER_FEELS_LIKE_ENG ) ){
            return hourWeatherVO.getFeelslikeTempEnglish() != null? Float.valueOf(hourWeatherVO.getFeelslikeTempEnglish()) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_FEELS_LIKE_METRIC ) ){
            return hourWeatherVO.getFeelslikeTempMetric() != null? Float.valueOf( hourWeatherVO.getFeelslikeTempMetric() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_HIGH_TEMP_ENG ) ){
            return hourWeatherVO.getHighTempEnglish() != null? Float.valueOf( hourWeatherVO.getHighTempEnglish() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_HIGH_TEMP_METRIC ) ){
            return hourWeatherVO.getHighTempMetric() != null? Float.valueOf( hourWeatherVO.getHighTempMetric() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_LOW_TEMP_ENG ) ){
            return hourWeatherVO.getLowTempEnglish() != null? Float.valueOf( hourWeatherVO.getLowTempEnglish() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_LOW_TEMP_METRIC ) ){
            return hourWeatherVO.getLowTempMetric() != null? Float.valueOf( hourWeatherVO.getLowTempMetric() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_HOUR ) ){
            return hourWeatherVO.getHour() != null? Integer.valueOf( hourWeatherVO.getHour() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_HUMIDITY ) ){
            return hourWeatherVO.getHumidity() != null? Float.valueOf( hourWeatherVO.getHumidity() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_DAY ) ){
            return hourWeatherVO.getDay() != null? Integer.valueOf( hourWeatherVO.getDay() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_MONTH ) ){
            return hourWeatherVO.getMonth() != null? Integer.valueOf( hourWeatherVO.getMonth() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_YEAR ) ){
            return hourWeatherVO.getYear() != null? Integer.valueOf( hourWeatherVO.getYear() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_SNOW_ENG ) ){
            return hourWeatherVO.getSnowEnglish() != null? Float.valueOf( hourWeatherVO.getSnowEnglish() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_SNOW_METRIC ) ){
            return hourWeatherVO.getSnowMetric() != null? Float.valueOf( hourWeatherVO.getSnowMetric() ) : new Float(-1);
        }
        return null;
    }

    public Object convertAttribute( String attribute, DayWeatherVO dayWeatherVO ){
        if( attribute.equals( Constants.WEATHER_CONDITION ) ){
            return dayWeatherVO.getCondition() != null? dayWeatherVO.getCondition() : "";
        }
        if( attribute.equals( Constants.WEATHER_DAY ) ){
            return dayWeatherVO.getDay() != null? Integer.valueOf( dayWeatherVO.getDay() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_DAY_OF_WEEK ) ){
            return dayWeatherVO.getDayOfWeek() != null? Integer.valueOf( dayWeatherVO.getDayOfWeek() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_MONTH ) ){
            return dayWeatherVO.getMonth() != null? Integer.valueOf( dayWeatherVO.getMonth() ) : new Integer(-1);
        }
        if( attribute.equals( Constants.WEATHER_YEAR ) ){
            return dayWeatherVO.getYear() != null? Float.valueOf( dayWeatherVO.getYear() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_HIGH_TEMP_ENG ) ){
            return dayWeatherVO.getHighTemp() != null? Float.valueOf( dayWeatherVO.getHighTemp() ) : new Float(-1);
        }
        if( attribute.equals( Constants.WEATHER_LOW_TEMP_ENG ) ){
            return dayWeatherVO.getLowTemp() != null? dayWeatherVO.getLowTemp() : new Float(-1);
        }
        return null;
    }


    public boolean areDatesEqual(Object weatherVO){
        boolean areEqual = false;
        try {
            if (weatherVO instanceof HourWeatherVO) {
                HourWeatherVO hourWeatherVO = (HourWeatherVO) weatherVO;
                if( getYear() == -1 || getMonth() == -1 || getDay() == -1 || getHour() == -1 ){
                    areEqual = true;
                }else{
                    // we cannot use minute because forecast reports only use integer hours
                    if( hourWeatherVO.getYear() == getYear() && hourWeatherVO.getMonth() == getMonth()
                            && hourWeatherVO.getDay() == getDay() && hourWeatherVO.getHour() == getHour()){
                        areEqual = true;
                    }
                }
            } else if (weatherVO instanceof DayWeatherVO) {
                DayWeatherVO dayWeatherVO = (DayWeatherVO) weatherVO;
                if( getYear() == -1 || getMonth() == -1  || getDay() == -1) {
                    areEqual = true;
                }else if( dayWeatherVO.getYear() == getYear() && dayWeatherVO.getMonth() == getMonth()
                        && dayWeatherVO.getDay() == getDay() ){
                    areEqual = true;
                }
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return areEqual;
    }

    public void onEvent(WeatherEvent event){
        ArrayList<DataObject> list =  new ArrayList<>( );
        list.addAll( event.getDailyWeather() );
        list.addAll( event.getHourlyWeather() );
        super.onEvent( list, null );
    }
}

