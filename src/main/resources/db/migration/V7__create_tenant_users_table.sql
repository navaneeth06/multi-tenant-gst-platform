CREATE TABLE tenant_users
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    tenant_id UUID NOT NULL,

    user_id   UUID NOT NULL,

    role_id   UUID NOT NULL,

    CONSTRAINT uk_tenant_user
        UNIQUE (tenant_id, user_id),

    CONSTRAINT fk_tenant_users_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_tenant_users_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_tenant_users_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE RESTRICT
);