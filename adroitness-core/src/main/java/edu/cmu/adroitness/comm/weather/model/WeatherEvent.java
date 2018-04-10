package edu.cmu.adroitness.comm.weather.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.weather.model.DayWeatherVO;
import edu.cmu.adroitness.client.services.weather.model.HourWeatherVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 9/15/15.
 */
public class WeatherEvent extends BaseEvent {
    private String place;
    private List<DayWeatherVO> dailyWeather;
    private List<HourWeatherVO> hourlyWeather;
    private boolean isError = false;
    private String errorMessage;
    private int forecastMode;

    private WeatherEvent(){ super(); }
    private WeatherEvent(int mbRequestId){ super( mbRequestId); }

    public static WeatherEvent build(){
        return new WeatherEvent();
    }
    public static WeatherEvent build(int mbRequestId){
        return new WeatherEvent(mbRequestId);
    }

    public String getPlace() {
        return place;
    }

    public WeatherEvent setPlace(String place) {
        this.place = place;
        return this;
    }

    public List<DayWeatherVO> getDailyWeather() {
        return  Util.clone( dailyWeather );
    }

    public List<String> getDailyWeatherString() {
        List<String> result = new ArrayList<>();
        for( DayWeatherVO dw : dailyWeather ){
            result.add( dw.toString() );
        }
        return result;
    }

    public WeatherEvent setDailyWeather(List<DayWeatherVO> dailyWeather) {
        this.dailyWeather = dailyWeather;
        return this;
    }

    public List<HourWeatherVO> getHourlyWeather() {
        return  Util.clone( hourlyWeather );
    }

    public List<String> getHourlyWeatherString() {
        List<String> result = new ArrayList<>();
        for( HourWeatherVO hw : hourlyWeather ){
            result.add( hw.toString() );
        }
        return result;
    }

    public WeatherEvent setHourlyWeather(List<HourWeatherVO> hourlyWeather) {
        this.hourlyWeather = Util.clone( hourlyWeather );
        return this;
    }

    public boolean isError() {
        return isError;
    }

    public WeatherEvent setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public WeatherEvent setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public int getForecastMode() {
        return forecastMode;
    }

    public WeatherEvent setForecastMode(int forecastMode) {
        this.forecastMode = forecastMode;
        return this;
    }
}
