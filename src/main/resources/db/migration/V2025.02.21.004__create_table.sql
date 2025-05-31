CREATE TABLE order_tb (
    id_order VARCHAR(36) NOT NULL PRIMARY KEY,
    dt_date TIMESTAMP DEFAULT NOW(),
    id_account VARCHAR(36) NOT NULL
);

CREATE TABLE order_item (
    id_item VARCHAR(36) NOT NULL PRIMARY KEY,
    id_order VARCHAR(36) NOT NULL,
    id_product VARCHAR(36) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,

    FOREIGN KEY (id_order) REFERENCES order_tb(id_order)
);
