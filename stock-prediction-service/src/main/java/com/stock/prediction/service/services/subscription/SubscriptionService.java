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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class SubscriptionService {

    private final CustomerService customerService;
    private final TimeUtils timeUtils;

    public SubscriptionService(CustomerService customerService, TimeUtils timeUtils) {
        this.customerService = customerService;
        this.timeUtils = timeUtils;
    }

    public SubscriptionResponse create(String customerId, SubscriptionRequest subscriptionRequest) {

        Customer existingCustomer = customerService.get(customerId);
        if(!Objects.isNull(existingCustomer.getActiveSubscription())){
            throw new BusinessException(ErrorType.MAX_SUBSCRIPTIONS_REACHED, String.format("Customer with id %s is allowed to have only one subscription active at a time.", customerId));
        }

        existingCustomer.setActiveSubscription(new Subscription(subscriptionRequest.getSubscriptionType()));
        existingCustomer.setSubscriptionUpdatedTs(timeUtils.now());

        customerService.save(existingCustomer);

        return new SubscriptionResponse(subscriptionRequest.getSubscriptionType());
    }

    public SubscriptionResponse update(String customerId, SubscriptionRequest subscriptionRequest) {

        Customer existingCustomer = customerService.get(customerId);
        LocalDateTime lastSubscriptionUpdate = existingCustomer.getSubscriptionUpdatedTs();
        LocalDateTime now = timeUtils.now();

        if(lastSubscriptionUpdate.plusMonths(1).isAfter(now)){
            throw new BusinessException(ErrorType.SUBSCRIPTION_CHANGE_NOT_ALLOWED, String.format("Cannot update subscription for customer with id %s, period is less then 1 month.", customerId));
        }

        existingCustomer.setActiveSubscription(new Subscription(subscriptionRequest.getSubscriptionType()));

        customerService.save(existingCustomer);

        return new SubscriptionResponse(subscriptionRequest.getSubscriptionType());
    }

    /**
     * For simplicity the benefits are hardcoded here, but we can as well take them from database or from an external service.
     */
    public SubscriptionBenefits benefits(SubscriptionType subscriptionType){
        switch (subscriptionType){

            case SILVER:
                return new SubscriptionBenefits(100L, 1L, 1L, ChronoUnit.MINUTES);
            case GOLD:
                return new SubscriptionBenefits(Long.MAX_VALUE, 1L, 10L, ChronoUnit.SECONDS);
            case DEMO:
            default:
                return new SubscriptionBenefits(10L, 1000L, 30L, ChronoUnit.DAYS);
        }
    }
}
