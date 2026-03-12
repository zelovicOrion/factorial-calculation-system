CREATE TABLE IF NOT EXISTS calculation (
    id VARCHAR(255) PRIMARY KEY,
    number BIGINT,
    threads INT,
    status VARCHAR(50),
    result CLOB
);