package com.Market.Data.Management.System.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@MappedSuperclass
public class Market {

    @JsonAlias({"last_trade_price", "lastTradePrice"})
    private BigDecimal lastTradePrice;

    @JsonAlias({"bid_price", "bidPrice"})
    private BigDecimal bidPrice;

    @JsonAlias({"mid_price", "midPrice"})
    private BigDecimal midPrice;

    @JsonAlias({"ask_price", "askPrice"})
    private BigDecimal askPrice;

    @JsonAlias({"market_time_stamp", "marketTimeStamp"})
    private Long marketTimeStamp;

    @JsonAlias({"depends_on_symbol", "dependsOnSymbol"})
    private String dependsOnSymbol;


    @JsonAlias({"last_coupon_date", "lastCouponDate"})
    private Date lastCouponDate;

    @JsonAlias({"interest_rate", "interestRate"})
    private BigDecimal interestRate;

    private BigDecimal volatility;

}
