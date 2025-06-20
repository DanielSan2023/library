CREATE TABLE IF NOT EXISTS user_info (
                                         customer_id VARCHAR(36) NOT NULL PRIMARY KEY,
                                         first_name VARCHAR(100) NOT NULL,
                                         last_name VARCHAR(100) NOT NULL,
                                         email VARCHAR(255) NOT NULL UNIQUE,
                                         password VARCHAR(255) NOT NULL,
                                         role VARCHAR(50) NOT NULL
);
