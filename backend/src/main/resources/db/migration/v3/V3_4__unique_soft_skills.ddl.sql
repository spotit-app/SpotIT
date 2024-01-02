ALTER TABLE soft_skill
ADD
    CONSTRAINT unique_soft_skills UNIQUE(
        user_account_id,
        soft_skill_name_id
    );
