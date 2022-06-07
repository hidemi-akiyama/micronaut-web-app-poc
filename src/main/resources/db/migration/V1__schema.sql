CREATE TABLE IF NOT EXISTS tenant_user
(
    id       BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS permission
(
    id        BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    tenant_user_id BIGINT NOT NULL,
    product    VARCHAR(255) NOT NULL,
    permission BOOLEAN      NOT NULL,
    FOREIGN KEY (tenant_user_id) REFERENCES tenant_user(id)
);
