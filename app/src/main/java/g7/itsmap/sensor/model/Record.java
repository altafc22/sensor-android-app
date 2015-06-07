package g7.itsmap.sensor.model;

import java.util.Date;

/**
 * Created by benjaminclanet on 03/05/2015.
 */
public class Record {

    private Date date;
    private double temperature;
    private double humidity;

    public Record() {}

    public Record(double temperature, double humidity, Date date) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
