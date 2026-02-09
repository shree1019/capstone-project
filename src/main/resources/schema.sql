CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(19,4) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transaction_logs (
    id BINARY(16) NOT NULL PRIMARY KEY,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255),
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_from_account FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transaction_to_account FOREIGN KEY (to_account_id) REFERENCES accounts(id)
);

