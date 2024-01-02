CREATE TABLE
    experience (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        company_name VARCHAR(50) NOT NULL,
        position VARCHAR(50) NOT NULL,
        start_date DATE NOT NULL,
        end_date DATE,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id)
    );
