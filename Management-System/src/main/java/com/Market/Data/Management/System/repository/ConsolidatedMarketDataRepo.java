package com.Market.Data.Management.System.repository;

import com.Market.Data.Management.System.model.ConsolidatedMarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsolidatedMarketDataRepo extends JpaRepository<com.Market.Data.Management.System.model.ConsolidatedMarketData, String> {
    Optional<ConsolidatedMarketData> findBySymbol(String symbol);

    List<ConsolidatedMarketData> findBySymbolIn(List<String> symbols);

    ConsolidatedMarketData deleteBySymbolAndSource(String symbol, String source);
}
