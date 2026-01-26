-- CREATE SCHEMA IF NOT EXISTS store;

CREATE TABLE IF NOT EXISTS store.products (
    product_id UUID PRIMARY KEY,
    product_name TEXT NOT NULL,
    description TEXT NOT NULL,
    image_src TEXT,
    quantity_state TEXT NOT NULL,
    product_state TEXT NOT NULL,
    product_category TEXT,
    price NUMERIC(10,2) NOT NULL,
    fragile BOOLEAN,
    width DOUBLE PRECISION CHECK (width > 1),
    height DOUBLE PRECISION CHECK (height > 1),
    depth DOUBLE PRECISION CHECK (DEPTH > 1),
    weight FLOAT
);