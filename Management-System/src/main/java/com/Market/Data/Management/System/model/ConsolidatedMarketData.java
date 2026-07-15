package com.Market.Data.Management.System.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "consolidated_market_data",
        uniqueConstraints = @UniqueConstraint(columnNames = {"symbol"},
                name = "symbol_unique"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedMarketData extends Market{
    @Id
    private String symbol;

    private BigDecimal accruedInterest;

    private BigDecimal theoreticalPrice;

    private String source;

    private Instant updatedAt;

    public ConsolidatedMarketData(String symbol) {
        this.symbol = symbol;
    }
}
