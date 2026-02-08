-- categories 初期データ
INSERT INTO categories (category_name) VALUES
('家電'),
('家具'),
('食品');

-- products 初期データ（テスト用）
INSERT INTO products (name, price, stock, category_id) VALUES
('Laptop', 100000, 10, 1),
('Desk', 20000, 5, 2),
('Apple', 300, 50, 3);