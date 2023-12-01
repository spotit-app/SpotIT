CREATE TABLE
    soft_skill_name (
        id SERIAL,
        name VARCHAR(50) NOT NULL UNIQUE,
        custom BOOLEAN NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    soft_skill (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        soft_skill_name_id INTEGER NOT NULL,
        skill_level INTEGER NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id),
        FOREIGN KEY (soft_skill_name_id) REFERENCES soft_skill_name (id)
    );
