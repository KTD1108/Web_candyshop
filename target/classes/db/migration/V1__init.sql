CREATE TABLE IF NOT EXISTS roles(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(120) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(120),
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
('Kẹo dẻo trái cây', 'keo-deo-trai-cay', 35000, 'gói', 'https://images.unsplash.com/photo-1548848955-3933a355c76a?q=80&w=600', 100, 'ACTIVE', 1, NOW()),
('Kẹo mút cầu vồng', 'keo-mut-cau-vong', 15000, 'cái', 'https://images.unsplash.com/photo-1580828343590-e55895b3a364?q=80&w=600', 200, 'ACTIVE', 1, NOW()),
('Kẹo bông gòn', 'keo-bong-gon', 20000, 'cây', 'https://images.unsplash.com/photo-1599408296945-49335c46d843?q=80&w=600', 150, 'ACTIVE', 1, NOW()),
('Kẹo dẻo gấu', 'keo-deo-gau', 40000, 'gói', 'https://images.unsplash.com/photo-1610442425993-3c3c8b8d2b1c?q=80&w=600', 120, 'ACTIVE', 1, NOW()),
('Kẹo cứng trái cây', 'keo-cung-trai-cay', 30000, 'hộp', 'https://images.unsplash.com/photo-1534733613192-c3a43c822136?q=80&w=600', 180, 'ACTIVE', 1, NOW()),

-- Bánh
('Bánh quy bơ', 'banh-quy-bo', 55000, 'hộp', 'https://images.unsplash.com/photo-1590089433536-818b627a4050?q=80&w=600', 50, 'ACTIVE', 2, NOW()),
('Bánh Macaron Pháp', 'banh-macaron-phap', 120000, 'hộp 6 cái', 'https://images.unsplash.com/photo-1558326567-98ae2405596b?q=80&w=600', 30, 'ACTIVE', 2, NOW()),
('Bánh Donut socola', 'banh-donut-socola', 22000, 'cái', 'https://images.unsplash.com/photo-1551106652-a5b85b492b33?q=80&w=600', 80, 'ACTIVE', 2, NOW()),
('Bánh Cupcake dâu', 'banh-cupcake-dau', 30000, 'cái', 'https://images.unsplash.com/photo-1614707267537-789725c3b352?q=80&w=600', 60, 'ACTIVE', 2, NOW()),
('Bánh sừng bò', 'banh-sung-bo', 25000, 'cái', 'https://images.unsplash.com/photo-1568584708742-9231cb6f0a8a?q=80&w=600', 90, 'ACTIVE', 2, NOW()),

-- Socola
('Socola đen 70%', 'socola-den-70', 85000, 'thanh', 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?q=80&w=600', 70, 'ACTIVE', 3, NOW()),
('Socola sữa hạnh nhân', 'socola-sua-hanh-nhan', 95000, 'thanh', 'https://images.unsplash.com/photo-1623872410483-69e0b288486a?q=80&w=600', 65, 'ACTIVE', 3, NOW()),
('Truffle socola', 'truffle-socola', 150000, 'hộp', 'https://images.unsplash.com/photo-1511382356534-90dec98b8c8b?q=80&w=600', 25, 'ACTIVE', 3, NOW()),
('Socola trắng dâu tây', 'socola-trang-dau-tay', 110000, 'thanh', 'https://images.unsplash.com/photo-1567606239624-36641a8a1b7a?q=80&w=600', 40, 'ACTIVE', 3, NOW()),
('Socola nóng', 'socola-nong', 65000, 'ly', 'https://images.unsplash.com/photo-1542990253-a7814de4e417?q=80&w=600', 100, 'ACTIVE', 3, NOW());

