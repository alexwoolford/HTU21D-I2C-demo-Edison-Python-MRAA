package io.woolford;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class TemperatureHumidityLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    @Scheduled(fixedDelay = 10000L)
    @PostConstruct
    private void logTermperature() throws InterruptedException {

        try {
            upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1);
            logger.info("Temperature: " + sensor.getTemperature() + "; humidity: " + sensor.getHumidity());
            sensor.delete();
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        
    }
}
