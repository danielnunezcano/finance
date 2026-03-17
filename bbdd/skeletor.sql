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

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(date);
CREATE INDEX IF NOT EXISTS idx_transactions_account ON transactions(account_id);