package com.stock.prediction.service.integration.service.stock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class DummyStockExchangeTest {

    @Mock
    private RandomNumberGeneratorClient stockPriceClient;

    @InjectMocks
    private DummyStockExchange dummyStockExchange;

    @Test
    public void priceMethodCalled_returnPrice(){

        // given
        String stockSymbol = "ABC";
        when(stockPriceClient.getRandomNumber()).thenReturn(List.of(12));

        // when
        Double result = dummyStockExchange.price(stockSymbol);

        // then
        assertEquals(12, result);
    }
}
