package com.stock.prediction.service.services.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
public class SubscriptionBenefits {

    private Long nrOfDifferentStocks;
    private Long maxNoOfCalls;
    private Long timeFrame;
    private ChronoUnit timeUnit;
}
