package com.Market.Data.Management.System.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    @Column(name = "data", columnDefinition = "LONGTEXT")
    private String data;

    private String user;

    private String action;

}
