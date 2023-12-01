CREATE TABLE
    foreign_language_name (
        id SERIAL,
        name VARCHAR(50) UNIQUE NOT NULL,
        flag_url VARCHAR(255) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    foreign_language (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        foreign_language_name_id INTEGER NOT NULL,
        language_level VARCHAR(10) NOT NULL, 
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id),
        FOREIGN KEY (foreign_language_name_id) REFERENCES foreign_language_name (id)
    );
