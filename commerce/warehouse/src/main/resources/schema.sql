CREATE SCHEMA IF NOT EXISTS warehouse;

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

CREATE TABLE IF NOT EXISTS warehouse.bookings (
    booking_id UUID PRIMARY KEY NOT NULL,
    order_id UUID NOT NULL,
    delivery_id UUID
);

CREATE TABLE IF NOT EXISTS warehouse.items (
    item_id UUID PRIMARY KEY NOT NULL,
    booking_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    CONSTRAINT fk_booking_items
        FOREIGN KEY (booking_id)
        REFERENCES warehouse.bookings(booking_id)
        ON DELETE CASCADE
);