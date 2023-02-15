package com.stock.prediction.service.integration.service.persitence.repository;

import com.stock.prediction.service.integration.service.persitence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
}
