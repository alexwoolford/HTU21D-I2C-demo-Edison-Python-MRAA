package io.woolford.temperaturehumidity;

public class SensorReading {

    private String host;
    private long timestamp;
    private float fahrenheit;
    private float humidity;

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
