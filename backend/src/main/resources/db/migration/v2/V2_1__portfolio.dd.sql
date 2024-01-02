CREATE TABLE
    portfolio(
        id SERIAL,
        user_account_id INTEGER UNIQUE NOT NULL,
        portfolio_url VARCHAR(255) UNIQUE NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id)
    );