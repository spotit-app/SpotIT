CREATE TABLE 
    address (
        id SERIAL, 
        country VARCHAR(100) NOT NULL, 
        zip_code VARCHAR(10), 
        city VARCHAR(10) NOT NULL, 
        street VARCHAR(100) NOT NULL, 
        PRIMARY KEY (id)
    );
