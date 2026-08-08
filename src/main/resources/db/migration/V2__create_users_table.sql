CREATE TABLE users
(

    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    tenant_id  UUID         NOT NULL,

    name       VARCHAR(255) NOT NULL,

    email      VARCHAR(255) NOT NULL,

    username   VARCHAR(255) NOT NULL,

    password   VARCHAR(255) NOT NULL,

    active     BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id),

    CONSTRAINT uk_user_tenant_username
        UNIQUE (tenant_id, username)
);