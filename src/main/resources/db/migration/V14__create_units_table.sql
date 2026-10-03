CREATE TABLE units
(
    id           UUID        NOT NULL,
    name         VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(10),

    CONSTRAINT pk_units
        PRIMARY KEY (id),

    CONSTRAINT uq_units_name
        UNIQUE (name)
);