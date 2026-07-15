package com.Market.Data.Management.System.repository;

import com.Market.Data.Management.System.dto.MarketDataDto;
import com.Market.Data.Management.System.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataRepo extends JpaRepository<MarketData, Long> {
    Optional<MarketData> findBySymbolAndSource(String symbol, String source);

    List<MarketData> findBySymbolOrderByMarketTimeStampAsc(String symbol);


    MarketData deleteBySymbolAndSource(String symbol, String source);
}

















//findBySymbolOrderByMarketTimestampAsc