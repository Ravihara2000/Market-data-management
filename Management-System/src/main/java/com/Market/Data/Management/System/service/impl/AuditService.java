package com.Market.Data.Management.System.service.impl;

import com.Market.Data.Management.System.model.Audit;
import com.Market.Data.Management.System.repository.AuditRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditRepo auditRepository;
    private final ObjectMapper objectMapper;

    public AuditService(AuditRepo auditRepository) {
        this.auditRepository = auditRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Async("virtualThreadExecutor")
    public void sendToAudit(Instant timestamp, Object data, String user, String action){
        try {
            Audit audit = new Audit();
            audit.setTimestamp(timestamp);
            audit.setData(objectMapper.writeValueAsString(data));
            audit.setUser(user);
            audit.setAction(action);

            auditRepository.save(audit);
            log.debug("Audit saved on thread {}", Thread.currentThread());
        }catch (Exception e){
            log.error("Error saving audit", e);
        }
    }

}
