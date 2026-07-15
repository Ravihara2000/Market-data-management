package com.Market.Data.Management.System.repository;

import com.Market.Data.Management.System.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<Audit, Long> {

}
