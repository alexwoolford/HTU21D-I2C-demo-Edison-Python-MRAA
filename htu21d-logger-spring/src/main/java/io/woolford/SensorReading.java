package io.woolford;

import java.time.Instant;

public class SensorReading {

    private String host;
    private String timestamp;
    private double fahrenheit;
    private double humidity;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
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
