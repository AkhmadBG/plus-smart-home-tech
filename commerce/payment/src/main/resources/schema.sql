CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment.payments (
    payment_id UUID PRIMARY KEY NOT NULL,
    order_id UUID NOT NULL,
    total_payment NUMERIC(19,2),
    delivery_total NUMERIC(19,2),
    fee_total NUMERIC(19,2),
    payment_state TEXT
);