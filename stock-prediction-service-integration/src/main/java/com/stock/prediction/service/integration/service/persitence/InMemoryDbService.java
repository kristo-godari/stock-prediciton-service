package com.stock.prediction.service.integration.service.persitence;

import com.stock.prediction.service.integration.service.persitence.entity.CustomerEntity;
import com.stock.prediction.service.integration.service.persitence.entity.StockEntity;
import com.stock.prediction.service.integration.service.persitence.repository.CustomerRepository;
import com.stock.prediction.service.integaration.api.service.persistence.Customer;
import com.stock.prediction.service.integaration.api.service.persistence.PersistenceService;
import com.stock.prediction.service.integaration.api.service.persistence.Subscription;
import com.stock.prediction.service.integaration.api.service.persistence.SubscriptionType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryDbService implements PersistenceService {

   private final CustomerRepository customerRepository;

    public InMemoryDbService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> get(String customerId) {

        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if(customerEntityOptional.isEmpty()){
            return Optional.empty();
        }
        CustomerEntity customerEntity = customerEntityOptional.get();

        Set<String> stockHistory = customerEntity.getStocksHistory().stream()
                .map(StockEntity::getStockSymbol).collect(Collectors.toSet());

        Subscription activeSubscription=null;
        if(!Objects.isNull(customerEntity.getActiveSubscription())){
            activeSubscription = new Subscription(SubscriptionType.valueOf(customerEntity.getActiveSubscription()));
        }

        Customer customer = Customer
                .builder()
                .customerId(customerEntity.getCustomerId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .activeSubscription(activeSubscription)
                .stocksHistory(stockHistory)
                .subscriptionUpdatedTs(customerEntity.getSubscriptionUpdatedTs())
                .build();

        return Optional.of(customer);
    }

    @Override
    public Customer save(Customer customer) {

        Set<StockEntity> stockHistory = customer.getStocksHistory().stream()
                .map(StockEntity::new).collect(Collectors.toSet());

        String activeSubscription=null;
        if(!Objects.isNull(customer.getActiveSubscription())){
            activeSubscription = customer.getActiveSubscription().getSubscriptionType().name();
        }

        CustomerEntity customerEntity = CustomerEntity
                .builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .activeSubscription(activeSubscription)
                .stocksHistory(stockHistory)
                .subscriptionUpdatedTs(customer.getSubscriptionUpdatedTs())
                .build();

        customerRepository.save(customerEntity);

        return customer;
    }
}
