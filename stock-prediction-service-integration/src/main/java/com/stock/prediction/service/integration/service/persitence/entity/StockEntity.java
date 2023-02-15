package com.stock.prediction.service.integration.service.persitence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table(name = "stocks")
public class StockEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String stockSymbol;

    @ManyToMany(mappedBy = "stocksHistory")
    private Set<CustomerEntity> customers;

    public StockEntity(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
}
