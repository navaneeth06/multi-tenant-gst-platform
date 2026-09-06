CREATE TABLE modules
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name        VARCHAR(255) NOT NULL,

    slug        VARCHAR(255) NOT NULL,

    description VARCHAR(500),

    CONSTRAINT uk_modules_name UNIQUE (name),

    CONSTRAINT uk_modules_slug UNIQUE (slug)
);