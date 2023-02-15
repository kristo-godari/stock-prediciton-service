package com.stock.prediction.service.services.customer;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.PersistenceService;
import com.stock.prediction.service.model.ErrorType;
import com.stock.prediction.service.model.customer.CreateCustomerRequest;
import com.stock.prediction.service.model.customer.CreateCustomerResponse;
import com.stock.prediction.service.utils.UuidGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class CustomerServiceTest {

    @Mock
    private PersistenceService persistenceService;

    @Mock
    private UuidGenerator uuidGenerator;

    @InjectMocks
    private CustomerService customerService;


    @Test
    public void save_givenCustomer_returnSavedCustomer(){
        // given
        Customer customerToBeSaved = Customer.builder().customerId("1233").build();

        when(persistenceService.save(eq(customerToBeSaved))).thenReturn(customerToBeSaved);

        // when
        Customer customerSaved = customerService.save(customerToBeSaved);

        // then
        assertEquals(customerToBeSaved, customerSaved);
    }

    @Test
    public void getCustomer_doNotExistsInDb_throwBusinessException(){
        // given
        String customerId = "123";

        when(persistenceService.get(eq(customerId))).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(BusinessException.class, () -> {
            customerService.get(customerId);
        });

        BusinessException businessException = (BusinessException) exception;

        // then
        assertEquals(ErrorType.INVALID_CUSTOMER, businessException.getErrorType());
        assertEquals("Customer with id 123 was not found in database.", businessException.getMessage());
    }

    @Test
    public void getCustomer_existsInDB_returnCustomer(){

        // given
        String customerId = "123";
        Customer expectedResult = Customer.builder().customerId(customerId).build();

        when(persistenceService.get(eq(customerId))).thenReturn(Optional.of(expectedResult));

        // when
        Customer actualResult = customerService.get(customerId);

        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void createCustomer_fromRequest_returnResponse(){

        // given
        String customerId = "123";
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("John");
        request.setFirstName("Doe");

        CreateCustomerResponse expectedResponse = new CreateCustomerResponse(customerId);

        Customer customerToSave = Customer
                .builder()
                .customerId(customerId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .stocksHistory(Collections.emptySet())
                .build();

        when(uuidGenerator.generate()).thenReturn(customerId);
        when(persistenceService.save(eq(customerToSave))).thenReturn(customerToSave);

        // when
        CreateCustomerResponse response = customerService.create(request);

        // then
        assertEquals(expectedResponse, response);
    }

}
