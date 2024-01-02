CREATE TABLE
    interest (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        name VARCHAR(50) NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id)
    );
