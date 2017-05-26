package io.woolford;

public class SensorReading {

    private double temperature;
    private double humidity;
    long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", timestamp=" + timestamp +
                '}';
    }

}
