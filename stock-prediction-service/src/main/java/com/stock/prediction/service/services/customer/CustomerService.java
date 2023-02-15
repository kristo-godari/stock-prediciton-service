package com.stock.prediction.service.services.customer;

import com.stock.prediction.service.exceptions.BusinessException;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.PersistenceService;
import com.stock.prediction.service.model.ErrorType;
import com.stock.prediction.service.model.customer.CreateCustomerRequest;
import com.stock.prediction.service.model.customer.CreateCustomerResponse;
import com.stock.prediction.service.utils.UuidGenerator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomerService {

    private final PersistenceService persistenceService;
    private final UuidGenerator uuidGenerator;

    public CustomerService(PersistenceService persistenceService, UuidGenerator uuidGenerator) {
        this.persistenceService = persistenceService;
        this.uuidGenerator = uuidGenerator;
    }

    public Customer get(String customerId){

        Optional<Customer> customer = persistenceService.get(customerId);
        if(customer.isEmpty()){
            throw new BusinessException(ErrorType.INVALID_CUSTOMER, String.format("Customer with id %s was not found in database.", customerId));
        }

        return customer.get();
    }

    public Customer save(Customer customer){
        return persistenceService.save(customer);
    }

    public CreateCustomerResponse create(CreateCustomerRequest createCustomerRequest) {

        String userId = uuidGenerator.generate();

        Customer customer = Customer
                .builder()
                .customerId(userId)
                .firstName(createCustomerRequest.getFirstName())
                .lastName(createCustomerRequest.getLastName())
                .stocksHistory(Collections.emptySet())
                .build();

        persistenceService.save(customer);

        return new CreateCustomerResponse(userId);
    }
}
