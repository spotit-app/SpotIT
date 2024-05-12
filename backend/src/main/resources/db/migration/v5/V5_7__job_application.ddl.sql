CREATE TABLE
    job_application (
        id SERIAL,
        job_offer_id INTEGER NOT NULL,
        portfolio_id INTEGER NOT NULL,
        application_status_id INTEGER NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (job_offer_id) REFERENCES job_offer (id),
        FOREIGN KEY (portfolio_id) REFERENCES portfolio (id),
        FOREIGN KEY (application_status_id) REFERENCES application_status (id)
    );