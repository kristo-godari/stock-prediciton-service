package com.stock.prediction.service.services.ratelimiter;

import com.stock.prediction.service.services.subscription.SubscriptionBenefits;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
public class InMemoryRateLimiterTest {

    @InjectMocks
    private InMemoryRateLimiter inMemoryRateLimiter;

    @Test
    public void limitIsReached_givenConsumerBenefits_returnTrue(){

        // given
        String customerId = "123";
        SubscriptionBenefits subscriptionBenefits = new SubscriptionBenefits(10L, 1L, 1L, ChronoUnit.NANOS);

        // when
        Boolean firstTry = inMemoryRateLimiter.isLimitReached(customerId, subscriptionBenefits);
        Boolean secondTry = inMemoryRateLimiter.isLimitReached(customerId, subscriptionBenefits);

        // then
        assertFalse(firstTry);
        assertTrue(secondTry);
    }

    @Test
    public void limitIsNotReached_givenConsumerBenefits_returnFalse(){
        // given
        String customerId = "123";
        SubscriptionBenefits subscriptionBenefits = new SubscriptionBenefits(10L, 2L, 1L, ChronoUnit.SECONDS);

        // when
        Boolean firstTry = inMemoryRateLimiter.isLimitReached(customerId, subscriptionBenefits);
        Boolean secondTry = inMemoryRateLimiter.isLimitReached(customerId, subscriptionBenefits);

        // then
        assertFalse(firstTry);
        assertFalse(secondTry);
    }

}
