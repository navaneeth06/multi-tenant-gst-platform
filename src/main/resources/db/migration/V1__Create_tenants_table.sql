CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tenants (

                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                         name VARCHAR(255) NOT NULL,

                         slug VARCHAR(255) NOT NULL UNIQUE,

                         gstin VARCHAR(15),

                         email VARCHAR(255) NOT NULL,

                         phone VARCHAR(20),

                         active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

);