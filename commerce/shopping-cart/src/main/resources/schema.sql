CREATE SCHEMA IF NOT EXISTS cart;

CREATE TABLE IF NOT EXISTS cart.carts (
    cart_id UUID PRIMARY KEY,
    cart_state TEXT NOT NULL,
    user_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS cart.items (
    item_id UUID PRIMARY KEY,
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id)
        REFERENCES cart.carts(cart_id)
        ON DELETE CASCADE
);