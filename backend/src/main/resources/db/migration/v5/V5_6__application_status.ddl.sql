CREATE TABLE 
    application_status (
        id SERIAL,
        name VARCHAR(50) UNIQUE NOT NULL,
        PRIMARY KEY (id)
    );