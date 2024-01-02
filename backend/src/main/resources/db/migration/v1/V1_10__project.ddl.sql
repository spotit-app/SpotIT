CREATE TABLE
    project (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        description TEXT NOT NULL,
        project_url VARCHAR(255),
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id)
    );
