package com.stock.prediction.service.integaration.api.service.persistence;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class Customer {

    private String customerId;
    private String firstName;
    private String lastName;
    private Subscription activeSubscription;
    private LocalDateTime subscriptionUpdatedTs;
    private Set<String> stocksHistory;

}
