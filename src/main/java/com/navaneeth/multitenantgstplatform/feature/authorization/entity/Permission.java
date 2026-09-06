package com.navaneeth.multitenantgstplatform.feature.authorization.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permissions_module_action",
                        columnNames = {"module_id", "action"}
                )
        }
)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "module_id",
            nullable = false
    )
    private Module module;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 500)
    private String description;
}