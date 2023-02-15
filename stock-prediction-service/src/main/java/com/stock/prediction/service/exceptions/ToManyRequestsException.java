package com.stock.prediction.service.exceptions;

public class ToManyRequestsException extends RuntimeException{
    public ToManyRequestsException(String message) {
        super(message);
    }
}
