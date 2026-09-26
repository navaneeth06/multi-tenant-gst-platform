package com.navaneeth.multitenantgstplatform.feature.gstregistration.entity;

import com.navaneeth.multitenantgstplatform.feature.master.entity.State;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "gst_registrations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_gst_registrations_tenant_gstin", columnNames = {"tenant_id", "gstin"}),
                @UniqueConstraint(name = "uq_gst_registrations_tenant_state", columnNames = {"tenant_id", "state_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class GSTRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(name = "gstin", nullable = false, length = 15)
    private String gstin;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}