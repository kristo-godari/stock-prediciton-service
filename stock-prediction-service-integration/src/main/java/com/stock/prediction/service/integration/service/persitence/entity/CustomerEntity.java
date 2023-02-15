package com.stock.prediction.service.integration.service.persitence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private String customerId;

    private String firstName;

    private String lastName;

    private String activeSubscription;

    private LocalDateTime subscriptionUpdatedTs;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "customers_stocks",
            joinColumns = @JoinColumn(name = "customers_customerId"),
            inverseJoinColumns = @JoinColumn(name = "stocks_id")
    )
    private Set<StockEntity> stocksHistory;

}
