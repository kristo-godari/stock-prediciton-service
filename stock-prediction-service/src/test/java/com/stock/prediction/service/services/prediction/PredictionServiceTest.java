package com.stock.prediction.service.services.prediction;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.exceptions.ToManyRequestsException;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.Subscription;
import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import com.stock.prediction.service.integaration.api.service.stock.StockPriceService;
import com.stock.prediction.service.model.ErrorType;
import com.stock.prediction.service.model.prediction.PredictionRequest;
import com.stock.prediction.service.model.prediction.PredictionResponse;
import com.stock.prediction.service.services.customer.CustomerService;
import com.stock.prediction.service.services.randomnumber.RandomNumberService;
import com.stock.prediction.service.services.ratelimiter.RateLimiterService;
import com.stock.prediction.service.services.subscription.SubscriptionBenefits;
import com.stock.prediction.service.services.subscription.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class PredictionServiceTest {

    @Mock
    private StockPriceService stockPriceService;

    @Mock
    private RandomNumberService randomNumberService;

    @Mock
    private CustomerService customerService;

    @Mock
    private RateLimiterService rateLimiterService;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private PredictionService predictionService;

    @Test
    public void predictCalled_customerDoesNotExist_throwsException(){
        // given
        String customerId = "123";
        PredictionRequest request = new PredictionRequest("ABC", LocalDateTime.now());

        when(customerService.get(eq(customerId))).thenThrow(new BusinessException(ErrorType.INVALID_CUSTOMER, String.format("Customer with id %s was not found in database.", customerId)));

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            predictionService.predict(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.INVALID_CUSTOMER, businessException.getErrorType());
        assertEquals("Customer with id 123 was not found in database.", businessException.getMessage());

    }

    @Test
    public void predictCalled_customerHasNoActiveSubscription_throwsException(){
        // given
        String customerId = "123";
        PredictionRequest request = new PredictionRequest("ABC", LocalDateTime.now());
        Customer customer = Customer
                .builder()
                .activeSubscription(null)
                .build();

        when(customerService.get(eq(customerId))).thenReturn(customer);

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            predictionService.predict(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.INVALID_SUBSCRIPTION, businessException.getErrorType());
        assertEquals("Customer with id 123 has not an active subscription.", businessException.getMessage());

    }

    @Test
    public void predictCalled_requestNoReached_throwsToManyRequestException(){
        // given
        String customerId = "123";
        PredictionRequest request = new PredictionRequest("ABC", LocalDateTime.now());
        Customer customer = Customer
                .builder()
                .activeSubscription(new Subscription(SubscriptionType.DEMO))
                .build();
        SubscriptionBenefits subscriptionBenefits = new SubscriptionBenefits(10L, 1000L, 30L, ChronoUnit.DAYS);

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(subscriptionService.benefits(eq(SubscriptionType.DEMO))).thenReturn(subscriptionBenefits);
        when(rateLimiterService.isLimitReached(eq(customerId), eq(subscriptionBenefits))).thenReturn(true);

        // when
        Exception exception = assertThrows(ToManyRequestsException.class, () -> {
            predictionService.predict(customerId, request);
        });

        ToManyRequestsException businessException = (ToManyRequestsException) exception;

        // then
        assertEquals("Too many requests for customer with id: 123", businessException.getMessage());

    }


    @Test
    public void predictCalled_stockLimitReached_throwsBusinessException(){
        // given
        String customerId = "123";
        PredictionRequest request = new PredictionRequest("BCD", LocalDateTime.now());
        Customer customer = Customer
                .builder()
                .activeSubscription(new Subscription(SubscriptionType.DEMO))
                .stocksHistory(Set.of("ABC", "ABD"))
                .build();
        SubscriptionBenefits subscriptionBenefits = new SubscriptionBenefits(1L, 1000L, 30L, ChronoUnit.DAYS);

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(subscriptionService.benefits(eq(SubscriptionType.DEMO))).thenReturn(subscriptionBenefits);
        when(rateLimiterService.isLimitReached(eq(customerId), eq(subscriptionBenefits))).thenReturn(false);

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            predictionService.predict(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.STOCK_LIMIT_REACHED, businessException.getErrorType());
        assertEquals("Customer with id 123 cannot have more than 1 different stocks.", businessException.getMessage());

    }

    @Test
    public void predictCalled_allWorksAsExpected_returnPrediction(){

        // given
        String customerId = "123";
        String stockSymbol = "BCD";

        PredictionRequest request = new PredictionRequest(stockSymbol, LocalDateTime.now());
        PredictionResponse expectedResponse = new PredictionResponse(12.0);

        Customer customer = Customer
                .builder()
                .activeSubscription(new Subscription(SubscriptionType.DEMO))
                .stocksHistory(new HashSet<>(Arrays.asList("ABC", "ABD")))
                .build();
        SubscriptionBenefits subscriptionBenefits = new SubscriptionBenefits(10L, 1000L, 30L, ChronoUnit.DAYS);

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(subscriptionService.benefits(eq(SubscriptionType.DEMO))).thenReturn(subscriptionBenefits);
        when(rateLimiterService.isLimitReached(eq(customerId), eq(subscriptionBenefits))).thenReturn(false);
        when(stockPriceService.price(eq(stockSymbol))).thenReturn(10.0);
        when(randomNumberService.get()).thenReturn(2);

        // when
        PredictionResponse response = predictionService.predict(customerId, request);

        // then
        assertEquals(expectedResponse, response);
        verify(customerService).save(any(Customer.class));

    }

}
