ALTER TABLE user_account
ADD
    COLUMN is_open BOOLEAN NOT NULL DEFAULT false;
