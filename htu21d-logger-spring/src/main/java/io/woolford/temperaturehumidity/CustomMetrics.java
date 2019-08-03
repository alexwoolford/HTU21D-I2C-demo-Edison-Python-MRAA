package io.woolford.temperaturehumidity;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class CustomMetrics {

    @Bean
    private void exposeCustomMetrics() {

        SensorReader sensorReader = new SensorReader();

        Gauge.builder("humidity", sensorReader, reader ->
        {
            try {
                return reader.getSensorReading().getHumidity();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return 0;
        }).strongReference(true)
          .register(Metrics.globalRegistry);

        Gauge.builder("fahrenheit", sensorReader, reader ->
        {
            try {
                return reader.getSensorReading().getFahrenheit();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return 0;
        }).strongReference(true)
          .register(Metrics.globalRegistry);

    }

}
