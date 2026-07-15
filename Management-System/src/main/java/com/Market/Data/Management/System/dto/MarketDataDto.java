package com.Market.Data.Management.System.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.annotation.Nullable;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MarketDataDto extends MarketDto{
    private Long id;

    private String symbol;

    private String source;
}
