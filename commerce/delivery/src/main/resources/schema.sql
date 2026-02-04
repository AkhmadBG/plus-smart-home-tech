CREATE SCHEMA IF NOT EXISTS delivery;

CREATE TABLE IF NOT EXISTS delivery.delivery (
    delivery_id UUID PRIMARY KEY NOT NULL,
    order_id UUID,
    from_address_id UUID,
    to_address_id UUID,
    delivery_state TEXT
);

CREATE TABLE IF NOT EXISTS delivery.addresses (
    address_id UUID PRIMARY KEY NOT NULL,
    country TEXT,
    city TEXT,
    street TEXT,
    house TEXT,
    flat TEXT
);