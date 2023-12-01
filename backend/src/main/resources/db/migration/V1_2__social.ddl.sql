CREATE TABLE
    social (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        name VARCHAR(30) NOT NULL,
        social_url VARCHAR(255) NOT NULL, 
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id)
    );
