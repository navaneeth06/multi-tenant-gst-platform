package com.navaneeth.multitenantgstplatform.feature.master.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "hsn_codes")
@Getter
@Setter
@NoArgsConstructor
public class HsnCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(length = 300)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gst_rate_id")
    private GSTRate gstRate;
}