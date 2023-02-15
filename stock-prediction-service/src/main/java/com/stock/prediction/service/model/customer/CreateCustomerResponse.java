package com.stock.prediction.service.model.customer;

import com.stock.prediction.service.model.BaseResponse;
import lombok.Data;

@Data
public class CreateCustomerResponse extends BaseResponse {

    private String customerId;

    public CreateCustomerResponse(String customerId) {
        super(null);
        this.customerId = customerId;
    }
}
