CREATE TABLE users
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    tenant_id     UUID REFERENCES tenants (id), -- nullable: NULL = SUPER_ADMIN (platform-level)

    name          VARCHAR(255) NOT NULL,

    email         VARCHAR(255) NOT NULL,

    username      VARCHAR(255) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    active        BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_tenant_id
    ON users (tenant_id);

CREATE UNIQUE INDEX uk_users_tenant_username
    ON users (tenant_id, username);

CREATE UNIQUE INDEX uk_users_tenant_email
    ON users (tenant_id, email);