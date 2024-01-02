CREATE TABLE
    tech_skill_name (
        id SERIAL,
        name VARCHAR(50) NOT NULL UNIQUE,
        logo_url VARCHAR(255),
        custom BOOLEAN NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    tech_skill (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        tech_skill_name_id INTEGER NOT NULL,
        skill_level INTEGER NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id),
        FOREIGN KEY (tech_skill_name_id) REFERENCES tech_skill_name (id)
    );
