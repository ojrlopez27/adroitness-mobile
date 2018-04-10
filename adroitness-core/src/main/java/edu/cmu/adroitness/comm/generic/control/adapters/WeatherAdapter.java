package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.generic.control.AwareServiceWrapper;
import edu.cmu.adroitness.client.services.location.model.LocationVO;
import edu.cmu.adroitness.client.services.location.view.LocationSettings;
import edu.cmu.adroitness.client.services.weather.control.WeatherService;

import java.util.List;

/**
 * Created by oscarr on 3/15/16.
 */
public final class WeatherAdapter extends ChannelAdapter {
    private static WeatherAdapter instance;

    private WeatherAdapter() {
        super();
    }

    public static WeatherAdapter getInstance() {
        if (instance == null) {
            instance = new WeatherAdapter();
        }
        return instance;
    }

    public Integer changeForecastMode(MBRequest request) {
        return mResourceLocator.lookupService(WeatherService.class)
                .changeForecastMode((String) request.get(Constants.WEATHER_PLACE));
    }

    public void subscribeForecast(MBRequest request) {
        Boolean isLocationActive = Boolean.valueOf(AwareServiceWrapper.getSetting(mContext,
                LocationSettings.STATUS_GOOGLE_FUSED_LOCATION));
        if (!isLocationActive) {
            AwareServiceWrapper.startPlugin(mContext, Constants.SERVICE_LOCATION);
        }
        LocationVO locationVO = new LocationVO();
        String place = (String) request.get(Constants.WEATHER_PLACE);
        if (place != null && !place.trim().equals("")) {
            locationVO.setSubArea((String) request.get(Constants.WEATHER_PLACE));
        } else {
            locationVO.setCountry((String) request.get(Constants.LOCATION_COUNTRY));
            locationVO.setCity((String) request.get(Constants.LOCATION_CITY));
            locationVO.setCountryCode((String) request.get(Constants.LOCATION_COUNTRY_CODE));
            locationVO.setStateCode((String) request.get(Constants.LOCATION_STATE_CODE));
            locationVO.setSubArea((String) request.get(Constants.WEATHER_PLACE));
        }
        int forecastMode = request.get(Constants.WEATHER_MODE) != null ? (Integer)
                request.get(Constants.WEATHER_MODE) : -1;
        long refreshTime = request.get(Constants.WEATHER_REFRESH_TIME) != null ? (Long)
                request.get(Constants.WEATHER_REFRESH_TIME) : -1;
        boolean forceRefresh = request.get(Constants.WEATHER_FORCE_REFRESH) != null ? (Boolean)
                request.get(Constants.WEATHER_FORCE_REFRESH) : false;
        mResourceLocator.lookupService(WeatherService.class).findWeatherData(locationVO,
                forecastMode, refreshTime, forceRefresh,
                (List) request.get(Constants.WEATHER_CONDITION_RULES));
    }

    public void unsubscribeForecast(MBRequest request) {
        String place = (String) request.get(Constants.WEATHER_PLACE);
        if (place != null && !place.equals("")) {
            mResourceLocator.lookupService(WeatherService.class).unsubscribe(place);
        }
    }


    public List getWeatherReport(MBRequest request) {
        Boolean isLocationActive = Boolean.valueOf(AwareServiceWrapper.getSetting(mContext,
                LocationSettings.STATUS_GOOGLE_FUSED_LOCATION));
        if (!isLocationActive) {
            AwareServiceWrapper.startPlugin(mContext, Constants.SERVICE_LOCATION);
        }
        int forecastMode = request.get(Constants.WEATHER_MODE) != null ? (Integer)
                request.get(Constants.WEATHER_MODE) : -1;
        String place = (String) request.get(Constants.WEATHER_PLACE);
        if (forecastMode == Constants.WEATHER_FORECAST_DAILY) {
            return mResourceLocator.lookupService(WeatherService.class).getDailyReport(place);
        } else {
            return mResourceLocator.lookupService(WeatherService.class).getHourlyReport(place);
        }
    }
}
