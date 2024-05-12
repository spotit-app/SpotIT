CREATE TABLE
    work_mode (
        id SERIAL,
        name VARCHAR(30) UNIQUE NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE 
    job_offer_work_mode (
        id SERIAL,
        job_offer_id INTEGER NOT NULL,
        work_mode_id INTEGER NOT NULL,
        FOREIGN KEY (job_offer_id) REFERENCES job_offer (id),
        FOREIGN KEY (work_mode_id) REFERENCES work_mode (id)
    );