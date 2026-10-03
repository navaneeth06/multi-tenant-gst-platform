CREATE TABLE gst_rates
(
    id           UUID          NOT NULL,
    rate_percent NUMERIC(5, 2) NOT NULL,
    active       BOOLEAN       NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_gst_rates
        PRIMARY KEY (id),

    CONSTRAINT uq_gst_rates_rate_percent
        UNIQUE (rate_percent)
);