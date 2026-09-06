CREATE TABLE permissions
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    module_id   UUID        NOT NULL,

    action      VARCHAR(50) NOT NULL,

    description VARCHAR(500),

    CONSTRAINT fk_permissions_module
        FOREIGN KEY (module_id)
            REFERENCES modules (id),

    CONSTRAINT uk_permissions_module_action
        UNIQUE (module_id, action)
);