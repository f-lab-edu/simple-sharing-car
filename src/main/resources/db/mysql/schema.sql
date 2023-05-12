CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(32),
    password VARCHAR(60),
    name VARCHAR(30),
    PRIMARY KEY(id)
);