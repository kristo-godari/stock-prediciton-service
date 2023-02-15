package com.stock.prediction.service.integaration.api.service.persistence;

import java.util.Optional;

public interface PersistenceService {

    Optional<Customer> get(String customerId);

    Customer save(Customer customer);
}
