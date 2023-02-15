package com.stock.prediction.service.services.ratelimiter;

import com.stock.prediction.service.services.subscription.SubscriptionBenefits;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryRateLimiter implements RateLimiterService{

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public Boolean isLimitReached(String customerId, SubscriptionBenefits subscriptionBenefits) {
        Bucket bucket = cache.computeIfAbsent(customerId, k -> newBucket(subscriptionBenefits));

        return !bucket.tryConsume(1);
    }

    private Bucket newBucket(SubscriptionBenefits subscriptionBenefits) {
        Duration duration = Duration.of(subscriptionBenefits.getTimeFrame(), subscriptionBenefits.getTimeUnit());
        Bandwidth limit = Bandwidth.simple(subscriptionBenefits.getMaxNoOfCalls(), duration);

        return Bucket.builder().addLimit(limit).build();
    }
}
