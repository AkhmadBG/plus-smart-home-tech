CREATE SCHEMA IF NOT EXISTS "order";

CREATE TABLE IF NOT EXISTS "order".orders (
    order_id UUID PRIMARY KEY NOT NULL,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    order_state TEXT,
    delivery_weight DOUBLE PRECISION CHECK (delivery_weight > 1),
    delivery_volume DOUBLE PRECISION CHECK (delivery_volume > 1),
    fragile BOOLEAN,
    total_price NUMERIC(19,2),
    delivery_price NUMERIC(10,2),
    product_price NUMERIC(10,2)
);

CREATE TABLE IF NOT EXISTS "order".items (
    product_item_id UUID PRIMARY KEY NOT NULL,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT fk_items_order
        FOREIGN KEY (order_id)
        REFERENCES "order".orders(order_id)
        ON DELETE CASCADE
);