package com.stock.prediction.service.controller;

import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import com.stock.prediction.service.model.customer.CreateCustomerRequest;
import com.stock.prediction.service.model.customer.CreateCustomerResponse;
import com.stock.prediction.service.model.prediction.PredictionRequest;
import com.stock.prediction.service.model.prediction.PredictionResponse;
import com.stock.prediction.service.model.subscription.SubscriptionRequest;
import com.stock.prediction.service.model.subscription.SubscriptionResponse;
import com.stock.prediction.service.services.customer.CustomerService;
import com.stock.prediction.service.services.prediction.PredictionService;
import com.stock.prediction.service.services.subscription.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class CustomerControllerTest {

    @Mock
    private PredictionService predictionService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;


    @Test
    public void predictMethodCalled_withCustomer_correctResponseReturned(){

        // given
        String customerId = "1233";
        PredictionRequest predictionRequest = new PredictionRequest();
        PredictionResponse expectedPredictionResponse = new PredictionResponse(12.2);

        when(predictionService.predict(eq(customerId), eq(predictionRequest)))
                .thenReturn(expectedPredictionResponse);

        // when
        PredictionResponse predictionResponse = customerController.predict(customerId, predictionRequest);

        // then
        assertEquals(expectedPredictionResponse, predictionResponse);
    }

    @Test
    public void createSubscriptionMethodCalled_withCustomer_correctResponseReturned(){

        // given
        String customerId = "1233";
        SubscriptionRequest request = new SubscriptionRequest();
        SubscriptionResponse expectedResponse = new SubscriptionResponse(SubscriptionType.DEMO);

        when(subscriptionService.create(eq(customerId), eq(request)))
                .thenReturn(expectedResponse);

        // when
        SubscriptionResponse actualResponse = customerController.createSubscription(customerId, request);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void updateSubscriptionMethodCalled_withCustomer_correctResponseReturned(){

        // given
        String customerId = "1233";
        SubscriptionRequest request = new SubscriptionRequest();
        SubscriptionResponse expectedResponse = new SubscriptionResponse(SubscriptionType.DEMO);

        when(subscriptionService.update(eq(customerId), eq(request)))
                .thenReturn(expectedResponse);

        // when
        SubscriptionResponse actualResponse = customerController.updateSubscription(customerId, request);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void createCustomerMethodCalled_withCustomer_correctResponseReturned(){

        // given
        CreateCustomerRequest request = new CreateCustomerRequest();
        CreateCustomerResponse expectedResponse = new CreateCustomerResponse("122222");

        when(customerService.create(eq(request)))
                .thenReturn(expectedResponse);

        // when
        CreateCustomerResponse actualResponse = customerController.createCustomer(request);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

}
