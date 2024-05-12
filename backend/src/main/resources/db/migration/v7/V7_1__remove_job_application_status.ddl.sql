ALTER TABLE job_application DROP COLUMN application_status_id;

DROP TABLE application_status;

ALTER TABLE job_application
ADD COLUMN application_status INTEGER NOT NULL;
