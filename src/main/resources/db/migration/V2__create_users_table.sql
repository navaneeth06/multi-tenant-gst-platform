CREATE TABLE users
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    tenant_id  UUID,

    name       VARCHAR(255) NOT NULL,

    email      VARCHAR(255) NOT NULL,

    username   VARCHAR(255) NOT NULL,

    password   VARCHAR(255) NOT NULL,

    active     BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id)
);

CREATE UNIQUE INDEX uk_users_global_username
    ON users (username)
    WHERE tenant_id IS NULL;

CREATE UNIQUE INDEX uk_users_tenant_username
    ON users (tenant_id, username)
    WHERE tenant_id IS NOT NULL;