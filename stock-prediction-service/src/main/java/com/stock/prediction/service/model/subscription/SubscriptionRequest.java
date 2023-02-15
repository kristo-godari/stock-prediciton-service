package com.stock.prediction.service.model.subscription;

import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionRequest {
    SubscriptionType subscriptionType;
}
