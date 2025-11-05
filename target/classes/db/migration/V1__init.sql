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
('Kẹo dẻo trái cây', 'keo-deo-trai-cay', 35000, 'gói', 'https://cf.shopee.vn/file/8c80ea7c75f14a19fd35da6dea084f48', 100, 'ACTIVE', 1, NOW()),
('Kẹo mút cầu vồng', 'keo-mut-cau-vong', 15000, 'cái', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m0uyy01zduct32', 200, 'ACTIVE', 1, NOW()),
('Kẹo bông gòn', 'keo-bong-gon', 20000, 'cây', 'https://down-vn.img.susercontent.com/file/74bcb2bba0a005082f5209d46013530a', 150, 'ACTIVE', 1, NOW()),
('Kẹo dẻo gấu', 'keo-deo-gau', 40000, 'gói', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m52jv07jdupke1', 120, 'ACTIVE', 1, NOW()),
('Kẹo cứng trái cây', 'keo-cung-trai-cay', 30000, 'hộp', 'https://ann.com.vn/wp-content/uploads/24894_vn-11134207-7r98o-lyr888n9v231d0_20240819114730-1.jpg', 180, 'ACTIVE', 1, NOW()),

-- Bánh
('Bánh quy bơ', 'banh-quy-bo', 55000, 'hộp', 'https://sieuthibsmart.com/wp-content/uploads/2023/06/banh-quy-bo-5.png', 50, 'ACTIVE', 2, NOW()),
('Bánh Macaron Pháp', 'banh-macaron-phap', 120000, 'hộp 6 cái', 'https://focusasiatravel.vn/wp-content/uploads/2021/10/banh-macaron-phap-3.jpg', 30, 'ACTIVE', 2, NOW()),
('Bánh Donut socola', 'banh-donut-socola', 22000, 'cái', 'https://i.ytimg.com/vi/I5J7DMg9l0U/maxresdefault.jpg', 80, 'ACTIVE', 2, NOW()),
('Bánh Cupcake dâu', 'banh-cupcake-dau', 30000, 'cái', 'https://daylambanh.edu.vn/wp-content/uploads/2017/03/cach-lam-banh-cupcake-dau.jpg', 60, 'ACTIVE', 2, NOW()),
('Bánh sừng bò', 'banh-sung-bo', 25000, 'cái', 'https://lamaca.vn/wp-content/uploads/2021/12/Banh-sung-bo-1.jpg', 90, 'ACTIVE', 2, NOW()),

-- Socola
('Socola đen 70%', 'socola-den-70', 85000, 'thanh', 'http://enjoycoffee.vn/wp-content/uploads/2023/11/z4885692003680_46d7987c2cffbedd5ce7dec7a86bcf65.jpg', 70, 'ACTIVE', 3, NOW()),
('Socola sữa hạnh nhân', 'socola-sua-hanh-nhan', 95000, 'thanh', 'https://havamall.com/wp-content/uploads/2021/03/Chocolate%20s%E1%BB%AFa%20b%E1%BB%8Dc%20h%E1%BA%A1nh%20nh%C3%A2n.jpg', 65, 'ACTIVE', 3, NOW()),
('Truffle socola', 'truffle-socola', 150000, 'hộp', 'https://bizweb.dktcdn.net/100/476/779/products/bce75840-dba1-4682-bdd3-9607613dfea7.jpg?v=1704512138363', 25, 'ACTIVE', 3, NOW()),
('Socola trắng dâu tây', 'socola-trang-dau-tay', 110000, 'thanh', 'https://i.pinimg.com/236x/10/2c/78/102c78bcb78061af4fcd76dceb4759f5.jpg', 40, 'ACTIVE', 3, NOW()),
('Socola nóng', 'socola-nong', 65000, 'ly', 'https://zingsweets.com/wp-content/uploads/2020/11/9-loi-ich-suc-khoe-dang-ngac-nhien-cua-socola-nong-3-1024x768.jpg?x75504', 100, 'ACTIVE', 3, NOW());

