package com.stock.prediction.service.integration.service.stock;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Used randomnumberapi.com to get a random number. This will simulate a real call to an external service to ge the stock price.
 */
@FeignClient(value = "stock-price-client", url = "${stock-price-client.baseUrl}")
public interface RandomNumberGeneratorClient {

    @RequestMapping(method = RequestMethod.GET, value = "/random?min=100&max=1000&count=1")
    List<Integer> getRandomNumber();

}
