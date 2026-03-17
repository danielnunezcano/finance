-- 1. Accounts Table (Savings, Checking, Cash, Investment)
CREATE TABLE IF NOT EXISTS accounts (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE,
                                        type VARCHAR(100), -- 'Savings', 'Checking', 'Cash', 'Investment'
                                        current_balance DECIMAL(15, 2) DEFAULT 0.00
);

-- 2. Categories Table (Groceries, Utilities, Capex...)
CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
                                          type VARCHAR(100), -- 'Income', 'Expense'
                                          is_capex BOOLEAN DEFAULT FALSE -- For the Pistachios project
);

-- 3. Transactions Table (Core movements)
CREATE TABLE IF NOT EXISTS transactions (
                                            id BIGSERIAL PRIMARY KEY,
                                            date DATE NOT NULL,
                                            amount DECIMAL(15, 2) NOT NULL,
                                            description VARCHAR(255),
                                            account_id BIGINT NOT NULL,
                                            category_id BIGINT NOT NULL,
                                            destination_account_id BIGINT, -- Null by default, used only for transfers

                                            CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id),
                                            CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id),
                                            CONSTRAINT fk_transaction_destination FOREIGN KEY (destination_account_id) REFERENCES accounts(id)
);

-- 4. Shared Expenses Table (The joint fund)
CREATE TABLE IF NOT EXISTS shared_expenses (
                                               id BIGSERIAL PRIMARY KEY,
                                               transaction_id BIGINT NOT NULL UNIQUE,
                                               paid_by VARCHAR(100) NOT NULL, -- 'Daniel' or 'Vanessa'
                                               daniel_debt DECIMAL(15, 2) DEFAULT 0.00,
                                               vanessa_debt DECIMAL(15, 2) DEFAULT 0.00,

                                               CONSTRAINT fk_shared_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.roles (
                                            id BIGSERIAL PRIMARY KEY,
                                            name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.users (
                                            id BIGSERIAL PRIMARY KEY,
                                            username VARCHAR(50) NOT NULL UNIQUE,
                                            password_hash VARCHAR(255) NOT NULL,
                                            email VARCHAR(100) UNIQUE,
                                            enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS public.user_roles (
                                                 user_id BIGINT NOT NULL,
                                                 role_id BIGINT NOT NULL,
                                                 PRIMARY KEY (user_id, role_id),
                                                 CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE,
                                                 CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES public.roles (id) ON DELETE CASCADE
);

-- Índices para optimizar búsquedas de seguridad
CREATE INDEX idx_users_username ON public.users (username);
CREATE INDEX idx_user_roles_user ON public.user_roles (user_id);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(date);
CREATE INDEX IF NOT EXISTS idx_transactions_account ON transactions(account_id);

-- New tables for Users and Roles
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Store encrypted password. NOTE: For real apps, encrypt this password.
    email VARCHAR(255) UNIQUE,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- e.g., 'ROLE_USER', 'ROLE_ADMIN'
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert a default role and user for testing
-- IMPORTANT: The password 'password' MUST be hashed using BCryptPasswordEncoder in a real application.
-- For demonstration purposes, a placeholder is used here. In a production environment,
-- this would be handled by a secure password hashing mechanism.
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO users (username, password_hash, email, enabled) VALUES ('testuser', 'encrypted_password_placeholder', 'testuser@example.com', TRUE); -- Replace 'encrypted_password_placeholder' with a bcrypt hash
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- Assuming user_id=1 and role_id=1 for the first inserted user/role. Adjust if necessary.
