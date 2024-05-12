CREATE TABLE 
    company (
        id SERIAL,
        user_account_id INTEGER NOT NULL,
        address_id INTEGER NOT NULL,
        name VARCHAR(100) NOT NULL,
        nip INTEGER NOT NULL,
        regon INTEGER,
        website_url VARCHAR(255),
        logo_url VARCHAR(255),
        PRIMARY KEY (id),
        FOREIGN KEY (user_account_id) REFERENCES user_account (id),
        FOREIGN KEY (address_id) REFERENCES address (id)
    );