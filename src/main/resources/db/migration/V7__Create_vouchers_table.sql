CREATE TABLE vouchers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    discount_type VARCHAR(50) NOT NULL,
    discount_value DECIMAL(19, 2) NOT NULL,
    max_discount_amount DECIMAL(19, 2),
    min_order_amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    usage_limit INT NOT NULL,
    current_usage INT NOT NULL DEFAULT 0
);
