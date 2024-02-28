CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(32),
    password VARCHAR(60),
    name VARCHAR(30),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT,
    users_id BIGINT,
    sharing_car_id BIGINT,
    payment_id BIGINT,
    res_start_time TIMESTAMP,
    res_end_time TIMESTAMP,
    status VARCHAR(20),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS sharing_car (
    id BIGINT AUTO_INCREMENT,
    standard_car_id BIGINT,
    sharing_zone_id BIGINT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS sharing_zone (
    id BIGINT AUTO_INCREMENT,
    latitude DECIMAL(18, 10),
    longitude DECIMAL(18, 10),
    name VARCHAR(100),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS standard_car (
    id BIGINT AUTO_INCREMENT,
    type VARCHAR(20),
    model VARCHAR(50),
    price_per_minute INT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS payment (
    id BIGINT AUTO_INCREMENT,
    price INT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    PRIMARY KEY(id)
);

