-- Create UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create TB_USERS table
CREATE TABLE IF NOT EXISTS TB_USERS
(
    user_id          UUID PRIMARY KEY,
    username         VARCHAR(50)  NOT NULL UNIQUE,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    full_name        VARCHAR(150) NOT NULL,
    user_status      VARCHAR(255) NOT NULL,
    user_type        VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(20),
    image_url        TEXT,
    creation_date    TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

-- Create indexes for better performance
CREATE INDEX idx_tb_users_username ON TB_USERS (username);
CREATE INDEX idx_tb_users_email ON TB_USERS (email);
CREATE INDEX idx_tb_users_creation_date ON TB_USERS (creation_date);

