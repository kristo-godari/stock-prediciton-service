package com.stock.prediction.service.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Extracted the functionality in a separate class, since is easier/more maintainable to mock in tests.
 */
@Component
public class TimeUtils {

    public LocalDateTime now(){
        return LocalDateTime.now();
    }
}
