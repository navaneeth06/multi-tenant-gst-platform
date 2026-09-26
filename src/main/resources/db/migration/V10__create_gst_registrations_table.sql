CREATE TABLE gst_registrations
(
    id         uuid PRIMARY KEY     DEFAULT gen_random_uuid(),
    tenant_id  uuid        NOT NULL REFERENCES tenants (id) ON DELETE CASCADE,
    state_id   uuid        NOT NULL REFERENCES states (id) ON DELETE RESTRICT,
    gstin      varchar(15) NOT NULL,
    status     varchar(20) NOT NULL DEFAULT 'ACTIVE',
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, gstin),
    UNIQUE (tenant_id, state_id) -- enforces: one GSTIN per state per tenant (the normal case)
);
CREATE INDEX idx_gst_registrations_tenant ON gst_registrations (tenant_id);