CREATE TABLE
    job_offer (
        id SERIAL,
        company_id INTEGER NOT NULL,
        work_experience_id INTEGER NOT NULL,
        name VARCHAR(100) NOT NULL,
        position VARCHAR(100) NOT NULL,
        description TEXT NOT NULL,
        min_salary INTEGER NOT NULL,
        max_salary INTEGER,
        benefits TEXT,
        due_date DATE NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (company_id) REFERENCES company (id),
        FOREIGN KEY (work_experience_id) REFERENCES work_experience (id)
    );

CREATE TABLE 
    job_offer_tech_skill_name (
        id SERIAL,
        job_offer_id INTEGER NOT NULL,
        tech_skill_name_id INTEGER NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (job_offer_id) REFERENCES job_offer (id),
        FOREIGN KEY (tech_skill_name_id) REFERENCES tech_skill_name (id)
    );

CREATE TABLE 
    job_offer_soft_skill_name (
        id SERIAL, 
        job_offer_id INTEGER NOT NULL, 
        soft_skill_name_id INTEGER NOT NULL, 
        PRIMARY KEY (id), 
        FOREIGN KEY (job_offer_id) REFERENCES job_offer (id), 
        FOREIGN KEY (soft_skill_name_id) REFERENCES soft_skill_name (id)
    );

CREATE TABLE 
    job_offer_foreign_language_name (
        id SERIAL,
        job_offer_id INTEGER NOT NULL, 
        foreign_language_name_id INTEGER NOT NULL, 
        PRIMARY KEY (id), 
        FOREIGN KEY (job_offer_id) REFERENCES job_offer (id), 
        FOREIGN KEY (foreign_language_name_id) REFERENCES foreign_language_name (id)
    );