CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE customers
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    tenant_id  UUID         NOT NULL,
    name       VARCHAR(200) NOT NULL,
    gstin      VARCHAR(15),
    phone      VARCHAR(20),
    email      CITEXT,
    address    TEXT,
    state_id   UUID         NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_customers_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id),

    CONSTRAINT fk_customers_state
        FOREIGN KEY (state_id)
            REFERENCES states (id)
);

CREATE INDEX idx_customers_tenant
    ON customers (tenant_id);