package com.stock.prediction.service.model.subscription;

import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import com.stock.prediction.service.model.BaseResponse;
import lombok.Data;

@Data
public class SubscriptionResponse extends BaseResponse {
    SubscriptionType newSubscription;

    public SubscriptionResponse(SubscriptionType newSubscription) {
        super(null);
        this.newSubscription = newSubscription;
    }
}
