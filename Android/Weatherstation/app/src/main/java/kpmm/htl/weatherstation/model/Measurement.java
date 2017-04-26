package kpmm.htl.weatherstation.model;

import java.sql.Timestamp;

/**
 * Created by ak47tehaxor on 12.01.17.
 */

public class Measurement implements Comparable<Measurement>{
    private float ambientTemperature;
    private float groundTemperature;
    private float airQuality;
    private float airPressure;
    private float humidity;
    private float windSpeed;
    private float windGustSpeed;
    private float rainfall;
    private Timestamp time;


    public Measurement(float ambientTemperature,
                       float groundTemperature,
                       float airQuality,
                       float airPressure,
                       float humidity,
                       float windSpeed,
                       float windGustSpeed,
                       float rainfall,
                       Timestamp time) {
        this.ambientTemperature = ambientTemperature;
        this.groundTemperature = groundTemperature;
        this.airQuality = airQuality;
        this.airPressure = airPressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windGustSpeed = windGustSpeed;
        this.rainfall = rainfall;
        this.time = time;
    }

    //region GETTER
    public float getAmbientTemperature() {
        return ambientTemperature;
    }

    public float getGroundTemperature() {
        return groundTemperature;
    }

    public float getAirQuality() {
        return airQuality;
    }

    public float getAirPressure() {
        return airPressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getWindGustSpeed() {
        return windGustSpeed;
    }

    public float getRainfall() {
        return rainfall;
    }

    public float getHumidity() {
        return humidity;
    }

    public Timestamp getTime() {
        return time;
    }
    //endregion

    @Override
    public int compareTo(Measurement measurement) {
        return time.compareTo(measurement.getTime())*-1;
    }
}
