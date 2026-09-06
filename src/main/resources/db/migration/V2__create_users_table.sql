CREATE TABLE users
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    name       VARCHAR(255) NOT NULL,

    email      VARCHAR(255) NOT NULL,

    username   VARCHAR(255) NOT NULL,

    password   VARCHAR(255) NOT NULL,

    active     BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_users_username
    ON users (username);

CREATE UNIQUE INDEX uk_users_email
    ON users (email)