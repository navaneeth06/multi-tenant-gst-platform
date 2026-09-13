package com.navaneeth.multitenantgstplatform.feature.master.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "states")
@Getter
@Setter
@NoArgsConstructor
public class State {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "gst_code", nullable = false, unique = true, length = 2)
    private String gstCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StateType type;

    public enum StateType {
        STATE,
        UNION_TERRITORY
    }
}