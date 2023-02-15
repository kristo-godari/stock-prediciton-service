package com.stock.prediction.service.model.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictionRequest {
    private String stockSymbol;
    private LocalDateTime timeFrame;
}
