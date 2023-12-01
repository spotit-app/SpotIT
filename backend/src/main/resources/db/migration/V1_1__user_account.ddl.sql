CREATE TABLE
    user_account (
        id SERIAL,
        auth0_id VARCHAR(100) UNIQUE NOT NULL, 
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        email VARCHAR(100) NOT NULL,
        phone_number VARCHAR(17),
        profile_picture_url VARCHAR(255),
        position VARCHAR(50),
        description TEXT,
        cv_clause TEXT,
        PRIMARY KEY (id)
    );
