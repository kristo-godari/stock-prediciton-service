package com.stock.prediction.service.services.prediction;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.exceptions.ToManyRequestsException;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.Subscription;
import com.stock.prediction.service.integaration.api.service.stock.StockPriceService;
import com.stock.prediction.service.model.ErrorType;
import com.stock.prediction.service.model.prediction.PredictionRequest;
import com.stock.prediction.service.model.prediction.PredictionResponse;
import com.stock.prediction.service.services.customer.CustomerService;
import com.stock.prediction.service.services.randomnumber.RandomNumberService;
import com.stock.prediction.service.services.ratelimiter.RateLimiterService;
import com.stock.prediction.service.services.subscription.SubscriptionBenefits;
import com.stock.prediction.service.services.subscription.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PredictionService {

    private final StockPriceService stockPriceService;
    private final RandomNumberService randomNumberService;
    private final CustomerService customerService;
    private final RateLimiterService rateLimiterService;
    private final SubscriptionService subscriptionService;

    public PredictionService(
        StockPriceService stockPriceService,
        RandomNumberService randomNumberService,
        CustomerService customerService,
        RateLimiterService rateLimiterService,
        SubscriptionService subscriptionService
    ) {
        this.stockPriceService = stockPriceService;
        this.randomNumberService = randomNumberService;
        this.customerService = customerService;
        this.rateLimiterService = rateLimiterService;
        this.subscriptionService = subscriptionService;
    }

    public PredictionResponse predict(String customerId, PredictionRequest predictionRequest){

        Customer existingCustomer = customerService.get(customerId);

        Subscription activeSubscription = existingCustomer.getActiveSubscription();
        if(Objects.isNull(activeSubscription)){
            throw new BusinessException(ErrorType.INVALID_SUBSCRIPTION, String.format("Customer with id %s has not an active subscription.", customerId));
        }

        SubscriptionBenefits subscriptionBenefits = subscriptionService.benefits(activeSubscription.getSubscriptionType());

        if(rateLimiterService.isLimitReached(customerId, subscriptionBenefits)){
            throw new ToManyRequestsException(String.format("Too many requests for customer with id: %s", customerId));
        }

        if(isStockLimitReached(existingCustomer, predictionRequest, subscriptionBenefits)){
            throw new BusinessException(ErrorType.STOCK_LIMIT_REACHED,
                    String.format("Customer with id %s cannot have more than %s different stocks.", customerId, subscriptionBenefits.getNrOfDifferentStocks()));
        }

        Double predictedPrice = this.stockPriceService.price(predictionRequest.getStockSymbol()) + randomNumberService.get();

        if(!existingCustomer.getStocksHistory().contains(predictionRequest.getStockSymbol())){
            existingCustomer.getStocksHistory().clear();
            existingCustomer.getStocksHistory().add(predictionRequest.getStockSymbol());

            customerService.save(existingCustomer);
        }

        return new PredictionResponse(predictedPrice);
    }

    private boolean isStockLimitReached(Customer existingCustomer, PredictionRequest predictionRequest, SubscriptionBenefits subscriptionBenefits){
        return !existingCustomer.getStocksHistory().contains(predictionRequest.getStockSymbol())
                && existingCustomer.getStocksHistory().size() >= subscriptionBenefits.getNrOfDifferentStocks();
    }
}
