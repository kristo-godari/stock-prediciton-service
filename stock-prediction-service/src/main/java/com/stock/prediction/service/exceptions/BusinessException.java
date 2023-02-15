package com.stock.prediction.service.exceptions;

import com.stock.prediction.service.model.ErrorType;

public class BusinessException extends RuntimeException{

    private final ErrorType errorType;

    public BusinessException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BusinessException(ErrorType errorType, String message, Throwable throwable) {
        super(message, throwable);
        this.errorType = errorType;
    }

    public BusinessException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
