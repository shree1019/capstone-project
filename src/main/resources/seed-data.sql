INSERT INTO accounts (holder_name, balance, status, version, last_updated)
VALUES
    ('Alice Smith', 1000.00, 'ACTIVE', 0, CURRENT_TIMESTAMP),
    ('Bob Johnson', 500.00, 'ACTIVE', 0, CURRENT_TIMESTAMP),
    ('Charlie Brown', 0.00, 'LOCKED', 0, CURRENT_TIMESTAMP);

