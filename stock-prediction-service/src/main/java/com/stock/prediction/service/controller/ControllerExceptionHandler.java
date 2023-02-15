package com.stock.prediction.service.controller;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.exceptions.ToManyRequestsException;
import com.stock.prediction.service.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessExceptions(BusinessException businessException){
      log.info("Business error. Message: {}", businessException.getMessage());

      final BaseResponse response = new BaseResponse(businessException.getErrorType());

      return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(ToManyRequestsException.class)
    protected ResponseEntity<Object> handleTooManyRequests(ToManyRequestsException toManyRequestsException){
        log.info("RateLimiter error. Message: {}", toManyRequestsException.getMessage());

        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleOtherExceptions(RuntimeException runtimeException){
        log.error("Internal Server Error. Message: {}", runtimeException.getMessage());

        return new ResponseEntity<>(new Object(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
