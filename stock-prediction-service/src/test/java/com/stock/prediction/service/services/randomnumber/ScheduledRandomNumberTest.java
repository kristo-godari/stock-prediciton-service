package com.stock.prediction.service.services.randomnumber;

import com.stock.prediction.service.properties.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
public class ScheduledRandomNumberTest {

    @Test
    public void generateMethodCalled_shouldReturnRandomNumber(){

        // given
        ConfigProperties.RandomNumber configRandomNo = new ConfigProperties.RandomNumber();
        configRandomNo.setMax(100);
        configRandomNo.setMin(-100);

        ConfigProperties configProperties = new ConfigProperties();
        configProperties.setRandomNumber(configRandomNo);

        ScheduledRandomNumber scheduledRandomNumber = new ScheduledRandomNumber(configProperties);

        // when
        Integer randomNumber = scheduledRandomNumber.get();

        // then
        assertTrue(randomNumber >= -100);
        assertTrue(randomNumber <= 100);
    }

}
