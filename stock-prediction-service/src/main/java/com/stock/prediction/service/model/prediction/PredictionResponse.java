package com.stock.prediction.service.model.prediction;

import com.stock.prediction.service.model.BaseResponse;
import lombok.Data;

@Data
public class PredictionResponse extends BaseResponse {
    private Double prediction;

    public PredictionResponse(Double prediction) {
        super(null);
        this.prediction = prediction;
    }

}

