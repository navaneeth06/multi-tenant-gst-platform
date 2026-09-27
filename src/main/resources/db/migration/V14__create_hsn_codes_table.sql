CREATE TABLE hsn_codes
(
    id          UUID        NOT NULL,
    code        VARCHAR(20) NOT NULL,
    description VARCHAR(300),
    gst_rate_id UUID,

    CONSTRAINT pk_hsn_codes
        PRIMARY KEY (id),

    CONSTRAINT uq_hsn_codes_code
        UNIQUE (code),

    CONSTRAINT fk_hsn_codes_gst_rate
        FOREIGN KEY (gst_rate_id)
            REFERENCES gst_rates (id)
);

CREATE INDEX idx_hsn_codes_gst_rate_id
    ON hsn_codes (gst_rate_id);