CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    label VARCHAR(255),
    price DOUBLE,
    quantity INTEGER,
);

INSERT INTO products (label, price, quantity) VALUES
('Café', 2.5, 20),
('Café Latte', 3, 15),
('Café Viennois', 3.2, 10);