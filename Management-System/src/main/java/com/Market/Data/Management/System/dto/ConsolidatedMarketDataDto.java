package com.Market.Data.Management.System.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedMarketDataDto extends MarketDto{

    private String symbol;

    private BigDecimal accruedInterest;

    private BigDecimal theoreticalPrice;

    private String source;

    private Instant updatedAt;
}
