CREATE TABLE states
(
    id         UUID                     NOT NULL,
    name       VARCHAR(100)             NOT NULL,
    code       VARCHAR(10)              NOT NULL,
    gst_code   VARCHAR(2)               NOT NULL,
    type       VARCHAR(20)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_states PRIMARY KEY (id),
    CONSTRAINT uq_states_name UNIQUE (name),
    CONSTRAINT uq_states_code UNIQUE (code),
    CONSTRAINT uq_states_gst_code UNIQUE (gst_code),

    CONSTRAINT chk_states_type
        CHECK (type IN ('STATE', 'UNION_TERRITORY'))
);