package io.woolford.temperaturehumidity;

import java.util.Date;

public class SensorReading {

    private String host;
    private Date timestamp;
    private float fahrenheit;
    private float humidity;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(float fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "host='" + host + '\'' +
                ", timestamp=" + timestamp +
                ", fahrenheit=" + fahrenheit +
                ", humidity=" + humidity +
                '}';
    }

}
