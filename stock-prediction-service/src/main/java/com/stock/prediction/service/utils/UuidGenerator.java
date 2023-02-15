package com.stock.prediction.service.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Extracted the functionality in a separate class, since is easier/more maintainable to mock in tests.
 */
@Component
public class UuidGenerator {

    public String generate(){
       return UUID.randomUUID().toString();
    }
}
