CREATE TABLE customers
(
    id         BIGSERIAL PRIMARY KEY,
    email      TEXT        NOT NULL UNIQUE,
    full_name  TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        TEXT        NOT NULL,
    price_cents INTEGER     NOT NULL CHECK (price_cents > 0),
    active      BOOLEAN     NOT NULL DEFAULT true,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT      NOT NULL REFERENCES customers (id),
    status      TEXT        NOT NULL CHECK (status IN ('NEW', 'PAID', 'CANCELLED')),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE order_items
(
    order_id    BIGINT  NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id  BIGINT  NOT NULL REFERENCES products (id),
    quantity    INTEGER NOT NULL CHECK (quantity > 0),
    price_cents INTEGER NOT NULL CHECK (price_cents > 0),
    PRIMARY KEY (order_id, product_id)
);

CREATE TABLE audit_log
(
    id          BIGSERIAL PRIMARY KEY,
    entity_type TEXT        NOT NULL,
    entity_id   BIGINT      NOT NULL,
    action      TEXT        NOT NULL,
    payload     JSONB,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ------------------------
-- Sample data
-- ------------------------

INSERT INTO customers (email, full_name)
VALUES ('alice@example.com', 'Alice Nowak'),
       ('bob@example.com', 'Bob Kowalski'),
       ('carol@example.com', 'Carol Zielinska');

INSERT INTO products (name, price_cents)
VALUES ('Laptop', 499999),
       ('Mechanical KB', 8999),
       ('Mouse', 3999),
       ('Monitor', 129999);

INSERT INTO orders (customer_id, status)
VALUES (1, 'PAID'),
       (1, 'NEW'),
       (2, 'CANCELLED'),
       (3, 'PAID');

INSERT INTO order_items (order_id, product_id, quantity, price_cents)
VALUES (1, 1, 1, 499999),
       (1, 2, 1, 8999),
       (2, 3, 2, 3999),
       (4, 4, 1, 129999),
       (4, 3, 1, 3999);

INSERT INTO audit_log (entity_type, entity_id, action, payload)
VALUES ('ORDER', 1, 'CREATED', '{
  "status": "PAID"
}'),
       ('ORDER', 2, 'CREATED', '{
         "status": "NEW"
       }'),
       ('ORDER', 3, 'CANCELLED', '{
         "reason": "customer request"
       }'),
       ('PRODUCT', 1, 'CREATED', '{
         "name": "Laptop"
       }');
