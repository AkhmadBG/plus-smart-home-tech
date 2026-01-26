-- CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.warehouses (
    warehouse_id UUID PRIMARY KEY,
    country TEXT NOT NULL,
    city TEXT NOT NULL,
    street TEXT NOT NULL,
    house TEXT NOT NULL,
    flat TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse.products (
    id UUID PRIMARY KEY,
    product_id UUID,
    fragile BOOLEAN,
    width DOUBLE PRECISION CHECK (width > 1),
    height DOUBLE PRECISION CHECK (height > 1),
    depth DOUBLE PRECISION CHECK (depth > 1),
    weight FLOAT,
    quantity INTEGER CHECK (quantity >= 0)
);