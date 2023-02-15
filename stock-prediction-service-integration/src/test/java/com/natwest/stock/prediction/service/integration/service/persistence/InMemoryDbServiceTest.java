package com.stock.prediction.service.integration.service.persistence;

import com.stock.prediction.service.integration.service.persitence.InMemoryDbService;
import com.stock.prediction.service.integration.service.persitence.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
public class InMemoryDbServiceTest {

    @Mock
    private  CustomerRepository customerRepository;

    @InjectMocks
    private InMemoryDbService inMemoryDbService;

    @Test
    public void getMethodIsCalled_noEntryInDB_returnEmpty(){
        // to be done later
        // given

        // when

        // then

        assertTrue(true);
    }

    @Test
    public void getMethodIsCalled_activeSubscriptionIsNull_shouldReturnCustomer(){
        // to be done later
        // given

        // when

        // then

        assertTrue(true);
    }

    @Test
    public void getMethodIsCalled_activeSubscriptionNotNull_shouldReturnCustomer(){
        // to be done later
        // given

        // when

        // then

        assertTrue(true);
    }

    @Test
    public void saveMethodIsCalled_activeSubscriptionIsNull_shouldSaveSuccessfully(){
        // to be done later
        // given

        // when

        // then

        assertTrue(true);
    }

    @Test
    public void saveMethodIsCalled_activeSubscriptionNotNull_shouldSaveSuccessfully(){
        // to be done later
        // given

        // when

        // then

        assertTrue(true);
    }
}
