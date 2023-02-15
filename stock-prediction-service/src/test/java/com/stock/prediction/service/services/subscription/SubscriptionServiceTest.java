package com.stock.prediction.service.services.subscription;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.Subscription;
import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import com.stock.prediction.service.model.ErrorType;
import com.stock.prediction.service.model.subscription.SubscriptionRequest;
import com.stock.prediction.service.model.subscription.SubscriptionResponse;
import com.stock.prediction.service.services.customer.CustomerService;
import com.stock.prediction.service.utils.TimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class SubscriptionServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private TimeUtils timeUtils;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    public void createCalled_customerDoesNotExist_throwsException(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);

        when(customerService.get(eq(customerId))).thenThrow(new BusinessException(ErrorType.INVALID_CUSTOMER, String.format("Customer with id %s was not found in database.", customerId)));

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            subscriptionService.create(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.INVALID_CUSTOMER, businessException.getErrorType());
        assertEquals("Customer with id 123 was not found in database.", businessException.getMessage());

    }


    @Test
    public void createCalled_customerHasAnActiveSubscription_throwsException(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);
        Customer customer = Customer
                .builder()
                .activeSubscription(new Subscription(SubscriptionType.DEMO))
                .build();

        when(customerService.get(eq(customerId))).thenReturn(customer);

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            subscriptionService.create(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.MAX_SUBSCRIPTIONS_REACHED, businessException.getErrorType());
        assertEquals("Customer with id 123 is allowed to have only one subscription active at a time.", businessException.getMessage());

    }


    @Test
    public void createCalled_flowExecutedWithSuccess_returnsSubscription(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);
        SubscriptionResponse expectedResponse = new SubscriptionResponse(SubscriptionType.DEMO);
        Customer customer = Customer
                .builder()
                .activeSubscription(null)
                .build();

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(timeUtils.now()).thenReturn(LocalDateTime.now());

        // when
        SubscriptionResponse response = subscriptionService.create(customerId, request);


        // then
        assertEquals(expectedResponse, response);
        verify(customerService).save(any(Customer.class));

    }

    @Test
    public void updateCalled_customerDoesNotExist_throwsException(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);

        when(customerService.get(eq(customerId))).thenThrow(new BusinessException(ErrorType.INVALID_CUSTOMER, String.format("Customer with id %s was not found in database.", customerId)));

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            subscriptionService.update(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.INVALID_CUSTOMER, businessException.getErrorType());
        assertEquals("Customer with id 123 was not found in database.", businessException.getMessage());

    }


    @Test
    public void updateCalled_customerHasAnActiveSubscription_throwsException(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);
        Customer customer = Customer
                .builder()
                .activeSubscription(new Subscription(SubscriptionType.DEMO))
                .subscriptionUpdatedTs(LocalDateTime.now())
                .build();

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(timeUtils.now()).thenReturn(LocalDateTime.now());

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            subscriptionService.update(customerId, request);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.SUBSCRIPTION_CHANGE_NOT_ALLOWED, businessException.getErrorType());
        assertEquals("Cannot update subscription for customer with id 123, period is less then 1 month.", businessException.getMessage());

    }


    @Test
    public void updateCalled_flowExecutedWithSuccess_returnsSubscription(){
        // given
        String customerId = "123";
        SubscriptionRequest request = new SubscriptionRequest(SubscriptionType.DEMO);
        SubscriptionResponse expectedResponse = new SubscriptionResponse(SubscriptionType.DEMO);
        Customer customer = Customer
                .builder()
                .activeSubscription(null)
                .subscriptionUpdatedTs(LocalDateTime.now().minusDays(35))
                .build();

        when(customerService.get(eq(customerId))).thenReturn(customer);
        when(timeUtils.now()).thenReturn(LocalDateTime.now());

        // when
        SubscriptionResponse response = subscriptionService.update(customerId, request);


        // then
        assertEquals(expectedResponse, response);
        verify(customerService).save(any(Customer.class));

    }
}
