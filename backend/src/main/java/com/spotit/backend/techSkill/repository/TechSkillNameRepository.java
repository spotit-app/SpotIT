package com.spotit.backend.techSkill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotit.backend.techSkill.model.TechSkillName;

@Repository
public interface TechSkillNameRepository extends JpaRepository<TechSkillName, Integer> {

    Optional<TechSkillName> findByName(String name);

    List<TechSkillName> findByCustom(Boolean custom);
}
