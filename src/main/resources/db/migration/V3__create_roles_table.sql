CREATE TABLE roles (

                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       tenant_id UUID,

                       name VARCHAR(255) NOT NULL,

                       slug VARCHAR(255) NOT NULL,

                       description TEXT,

                       CONSTRAINT fk_roles_tenant
                           FOREIGN KEY (tenant_id)
                               REFERENCES tenants(id)
);

CREATE UNIQUE INDEX uk_system_role_slug
    ON roles (slug)
    WHERE tenant_id IS NULL;

CREATE UNIQUE INDEX uk_tenant_role_slug
    ON roles (tenant_id, slug)
    WHERE tenant_id IS NOT NULL;