package com.stock.prediction.service.util;

import com.stock.prediction.service.utils.UuidGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SpringExtension.class})
public class UuidGeneratorTest {

    @InjectMocks
    private UuidGenerator uuidGenerator;

    @Test
    public void generateMethodIsCalled_uuidIsGenerated(){

        // when
        String uuid = uuidGenerator.generate();

        // then
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
    }
}
