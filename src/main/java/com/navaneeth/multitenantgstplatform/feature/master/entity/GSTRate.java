package com.navaneeth.multitenantgstplatform.feature.master.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "gst_rates")
@Getter
@Setter
@NoArgsConstructor
public class GSTRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "rate_percent", nullable = false, precision = 5, scale = 2, unique = true)
    private BigDecimal ratePercent;

    @Column(nullable = false)
    private boolean active = true;
}