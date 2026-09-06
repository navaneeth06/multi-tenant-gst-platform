package com.navaneeth.multitenantgstplatform.feature.authorization.entity;

import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "tenant_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_user",
                        columnNames = {"tenant_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tenant_id",
            nullable = false
    )
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private Role role;
}