ALTER TABLE tech_skill
ADD
    CONSTRAINT unique_tech_skills UNIQUE(
        user_account_id,
        tech_skill_name_id
    );
