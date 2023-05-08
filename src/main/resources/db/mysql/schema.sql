DROP TABLE IF exists users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(32),
    password VARCHAR(50),
    name VARCHAR(30),
    PRIMARY KEY(id)
);