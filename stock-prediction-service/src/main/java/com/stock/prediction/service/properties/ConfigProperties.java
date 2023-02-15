package com.stock.prediction.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "stock-prediction-service")
public class ConfigProperties {

    RandomNumber randomNumber;

    @Data
    public static class RandomNumber {
        private Integer min;
        private Integer max;
    }
}
