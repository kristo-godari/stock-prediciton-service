package com.stock.prediction.service.services.ratelimiter;

import com.stock.prediction.service.services.subscription.SubscriptionBenefits;

public interface RateLimiterService {

    Boolean isLimitReached(String customerId, SubscriptionBenefits subscriptionBenefits);
}
