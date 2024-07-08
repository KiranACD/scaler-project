CREATE TABLE order_item
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    created_at          datetime NULL,
    updated_at          datetime NULL,
    is_deleted          BIT(1) NOT NULL,
    product_id          BIGINT NOT NULL,
    quantity            INT NOT NULL,
    price               DOUBLE NOT NULL,
    order_id            BIGINT NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    created_at          datetime NULL,
    updated_at          datetime NULL,
    is_deleted          BIT(1) NOT NULL,
    user_id             BIGINT NOT NULL,
    order_date          datetime NOT NULL,
    total_price         double NOT NULL,
    order_status              VARCHAR(255) NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDERS FOREIGN KEY (order_id) REFERENCES orders (id);