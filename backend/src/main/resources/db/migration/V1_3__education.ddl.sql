CREATE TABLE
    education_level (
        id SERIAL,
        name VARCHAR(50) NOT NULL UNIQUE,
        custom BOOLEAN NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    education (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        education_level_id INTEGER NOT NULL,
        school_name VARCHAR(50) NOT NULL,
        faculty VARCHAR(50),
        start_date DATE NOT NULL,
        end_date DATE,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id),
        FOREIGN KEY (education_level_id) REFERENCES education_level (id)
    );
