package io.woolford;

public class SensorReading {

    private String host;
    private long timestamp;
    private double fahrenheit;
    private double humidity;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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
