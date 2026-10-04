CREATE TABLE products
(
    id          UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    tenant_id   UUID           NOT NULL,
    name        VARCHAR(200)   NOT NULL,
    sku         VARCHAR(100)   NOT NULL,
    price       NUMERIC(18, 2) NOT NULL,
    hsn_code_id UUID           NOT NULL,
    gst_rate_id UUID           NOT NULL,
    unit_id     UUID           NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_products_tenant_sku
        UNIQUE (tenant_id, sku),

    CONSTRAINT chk_products_price_non_negative
        CHECK (price >= 0),

    CONSTRAINT fk_products_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants (id),

    CONSTRAINT fk_products_hsn_code
        FOREIGN KEY (hsn_code_id)
            REFERENCES hsn_codes (id),

    CONSTRAINT fk_products_gst_rate
        FOREIGN KEY (gst_rate_id)
            REFERENCES gst_rates (id),

    CONSTRAINT fk_products_unit
        FOREIGN KEY (unit_id)
            REFERENCES units (id)
);

CREATE INDEX idx_products_tenant
    ON products (tenant_id);