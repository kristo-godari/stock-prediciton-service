package com.stock.prediction.service.services.randomnumber;

import com.stock.prediction.service.properties.ConfigProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ScheduledRandomNumber implements RandomNumberService{

    private Integer randomNumber;
    private final ConfigProperties configProperties;

    public ScheduledRandomNumber(ConfigProperties configProperties) {
        this.configProperties = configProperties;
        this.randomNumber = generateRandomNumber(configProperties.getRandomNumber().getMin(), configProperties.getRandomNumber().getMax());
    }

    @Scheduled(fixedRate = 1000)
    private void generate() throws InterruptedException {
        this.randomNumber = generateRandomNumber(configProperties.getRandomNumber().getMin(), configProperties.getRandomNumber().getMax());
    }

    @Override
    public Integer get() {
        return this.randomNumber;
    }

    private Integer generateRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
