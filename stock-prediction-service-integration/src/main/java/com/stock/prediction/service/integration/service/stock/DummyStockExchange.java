package com.stock.prediction.service.integration.service.stock;

import com.stock.prediction.service.integaration.api.service.stock.StockPriceService;
import org.springframework.stereotype.Service;

/**
 * A dummy, but functional stock exchange to get the stock price.
 * I have used a real api to get a random number, that it's supposed to represent the stock price.
 */
@Service
public class DummyStockExchange implements StockPriceService {

    private final RandomNumberGeneratorClient stockPriceClient;

    public DummyStockExchange(RandomNumberGeneratorClient stockPriceClient) {
        this.stockPriceClient = stockPriceClient;
    }

    @Override
    public Double price(String stockSymbol) {
        return this.stockPriceClient.getRandomNumber().stream().findFirst().get().doubleValue();
    }
}
