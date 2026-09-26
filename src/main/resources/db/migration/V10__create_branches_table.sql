CREATE TABLE branches
(
    id         UUID                     NOT NULL,
    tenant_id  UUID                     NOT NULL,
    name       VARCHAR(150)             NOT NULL,
    address    TEXT,
    state_id   UUID,
    pincode    VARCHAR(10),
    status     VARCHAR(20)              NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_branches
        PRIMARY KEY (id),

    CONSTRAINT fk_branches_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_branches_state
        FOREIGN KEY (state_id)
            REFERENCES states (id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_branches_tenant_id
    ON branches (tenant_id);

CREATE INDEX idx_branches_state_id
    ON branches (state_id);