ALTER TABLE orders ADD COLUMN payment_method VARCHAR(255) NULL;
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(255) NULL;
ALTER TABLE orders ADD COLUMN order_notes TEXT NULL;
ALTER TABLE orders ADD COLUMN voucher_code VARCHAR(255) NULL;
ALTER TABLE orders ADD COLUMN discount_amount DECIMAL(12, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN shipping_fee DECIMAL(12, 2) NOT NULL DEFAULT 0;

UPDATE orders SET shipping_fee = 0 WHERE shipping_fee IS NULL;
