CREATE TABLE IF NOT EXISTS roles(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(120) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(120),
  address VARCHAR(255) NULL,
  phone VARCHAR(255) NULL,
  enabled TINYINT(1) DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles(
  user_id BIGINT, role_id BIGINT,
  PRIMARY KEY(user_id, role_id),
  CONSTRAINT fk_ur_user FOREIGN KEY(user_id) REFERENCES users(id),
  CONSTRAINT fk_ur_role FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS categories(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  slug VARCHAR(150) UNIQUE NOT NULL,
  parent_id BIGINT NULL,
  CONSTRAINT fk_cat_parent FOREIGN KEY(parent_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS products(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  slug VARCHAR(220) UNIQUE NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  unit VARCHAR(30) DEFAULT 'hộp',
  thumbnail_url VARCHAR(255),
  stock_qty INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  description TEXT,
  category_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_prod_cat FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS carts(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT UNIQUE,
  CONSTRAINT fk_cart_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cart_items(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  cart_id BIGINT,
  product_id BIGINT,
  quantity INT NOT NULL,
  UNIQUE(cart_id, product_id),
  CONSTRAINT fk_ci_cart FOREIGN KEY(cart_id) REFERENCES carts(id),
  CONSTRAINT fk_ci_product FOREIGN KEY(product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS orders(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  total_amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING',
  shipping_address VARCHAR(255),
  shipping_phone VARCHAR(255) NULL,
  payment_method VARCHAR(255) NULL,
  shipping_method VARCHAR(255) NULL,
  order_notes TEXT NULL,
  voucher_code VARCHAR(255) NULL,
  discount_amount DECIMAL(12, 2) DEFAULT 0,
  shipping_fee DECIMAL(12, 2) NOT NULL DEFAULT 0,
  internal_notes VARCHAR(255) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_order_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_items(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT,
  product_id BIGINT,
  price DECIMAL(12,2) NOT NULL,
  quantity INT NOT NULL,
  CONSTRAINT fk_oi_order FOREIGN KEY(order_id) REFERENCES orders(id),
  CONSTRAINT fk_oi_product FOREIGN KEY(product_id) REFERENCES products(id)
);

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

-- Initial roles
INSERT IGNORE INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Seed Categories
INSERT INTO categories (id, name, slug) VALUES
(1, 'Kẹo', 'keo'),
(2, 'Bánh', 'banh'),
(3, 'Sô-cô-la', 'socola');

-- Seed Products
INSERT INTO products (name, slug, price, unit, thumbnail_url, stock_qty, status, category_id, created_at) VALUES
-- Kẹo
('Kẹo dẻo trái cây', 'keo-deo-trai-cay', 35000, 'gói', '/images/keo-deo-trai-cay.jpg', 100, 'ACTIVE', 1, NOW()),
('Kẹo mút cầu vồng', 'keo-mut-cau-vong', 15000, 'cái', '/images/keo-mut-cau-vong.jpg', 200, 'ACTIVE', 1, NOW()),
('Kẹo bông gòn', 'keo-bong-gon', 20000, 'cây', '/images/keo-bong-gon.jpg', 150, 'ACTIVE', 1, NOW()),
('Kẹo dẻo gấu', 'keo-deo-gau', 40000, 'gói', '/images/keo-deo-gau.jpg', 120, 'ACTIVE', 1, NOW()),
('Kẹo cứng trái cây', 'keo-cung-trai-cay', 30000, 'hộp', '/images/keo-cung-trai-cay.jpg', 180, 'ACTIVE', 1, NOW()),

-- Bánh
('Bánh quy bơ', 'banh-quy-bo', 55000, 'hộp', '/images/banh-quy-bo.jpg', 50, 'ACTIVE', 2, NOW()),
('Bánh Macaron Pháp', 'banh-macaron-phap', 120000, 'hộp 6 cái', '/images/banh-macaron-phap.jpg', 30, 'ACTIVE', 2, NOW()),
('Bánh Donut socola', 'banh-donut-socola', 22000, 'cái', '/images/banh-donut-socola.jpg', 80, 'ACTIVE', 2, NOW()),
('Bánh Cupcake dâu', 'banh-cupcake-dau', 30000, 'cái', '/images/banh-cupcake-dau.jpg', 60, 'ACTIVE', 2, NOW()),
('Bánh sừng bò', 'banh-sung-bo', 25000, 'cái', '/images/banh-sung-bo.jpg', 90, 'ACTIVE', 2, NOW()),

-- Socola
('Socola đen 70%', 'socola-den-70', 85000, 'thanh', '/images/socola-den-70.jpg', 70, 'ACTIVE', 3, NOW()),
('Socola sữa hạnh nhân', 'socola-sua-hanh-nhan', 95000, 'thanh', '/images/socola-sua-hanh-nhan.jpg', 65, 'ACTIVE', 3, NOW()),
('Truffle socola', 'truffle-socola', 150000, 'hộp', '/images/truffle-socola.jpg', 25, 'ACTIVE', 3, NOW()),
('Socola trắng dâu tây', 'socola-trang-dau-tay', 110000, 'thanh', '/images/socola-trang-dau-tay.jpg', 40, 'ACTIVE', 3, NOW()),
('Socola nóng', 'socola-nong', 65000, 'ly', '/images/socola-nong.jpg', 100, 'ACTIVE', 3, NOW());