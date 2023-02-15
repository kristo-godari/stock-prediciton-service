package com.stock.prediction.service.util;

import com.stock.prediction.service.utils.TimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
public class TimeUtilsTest {

    @InjectMocks
    private TimeUtils timeUtils;

    @Test
    public void nowMethodCalled_afterCurrentTime_nowShouldBeAfterCurrentTime(){

        // given
        LocalDateTime currentTime = LocalDateTime.now();

        // when
        LocalDateTime now = timeUtils.now();

        // then
        assertTrue(now.isAfter(currentTime));
    }
}
