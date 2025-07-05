DROP TABLE IF EXISTS prices;
CREATE TABLE prices
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id      BIGINT         NOT NULL,
    valid_from    TIMESTAMP      NOT NULL,
    valid_until   TIMESTAMP      NOT NULL,
    rate          BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    priority      INT            NOT NULL,
    final_price   DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(3)     NOT NULL
);

DROP INDEX IF EXISTS idx_prices_product_brand_dates;
CREATE INDEX idx_prices_product_brand_dates
    ON prices (product_id, brand_id, valid_from, valid_until, priority DESC);
