package com.Market.Data.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditPOJO {

    private Instant timestamp;

    private Object data;

    private String user;

    private String action;
}
