-- Create UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create TB_USERS table with all optimizations
CREATE TABLE IF NOT EXISTS TB_USERS
(
    user_id          UUID PRIMARY KEY,
    username         VARCHAR(50)  NOT NULL UNIQUE,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    full_name        VARCHAR(150) NOT NULL,
    user_status      VARCHAR(50)  NOT NULL,
    user_type        VARCHAR(50)  NOT NULL,
    phone_number     VARCHAR(20),
    image_url        BYTEA,
    creation_date    TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

-- Add comments for documentation
COMMENT ON TABLE TB_USERS IS 'Tabela de usuários do sistema';
COMMENT ON COLUMN TB_USERS.image_url IS 'Imagem de perfil compactada em formato binário (BYTEA) - 40-50KB máximo';
COMMENT ON COLUMN TB_USERS.user_status IS 'Status do usuário (ACTIVE, INACTIVE)';
COMMENT ON COLUMN TB_USERS.user_type IS 'Tipo de usuário (USER, ADMIN)';

-- Create indexes for better performance
CREATE INDEX idx_tb_users_username ON TB_USERS (username);
CREATE INDEX idx_tb_users_email ON TB_USERS (email);
CREATE INDEX idx_tb_users_creation_date ON TB_USERS (creation_date);

