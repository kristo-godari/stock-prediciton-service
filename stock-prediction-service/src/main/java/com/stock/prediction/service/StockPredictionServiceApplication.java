package com.stock.prediction.service;

import com.stock.prediction.service.properties.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableConfigurationProperties(ConfigProperties.class)
@SpringBootApplication
public class StockPredictionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPredictionServiceApplication.class, args);
	}

}
