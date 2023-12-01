package com.spotit.backend.softSkill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotit.backend.softSkill.model.SoftSkillName;

@Repository
public interface SoftSkillNameRepository extends JpaRepository<SoftSkillName, Integer> {

    Optional<SoftSkillName> findByName(String name);

    List<SoftSkillName> findByCustom(Boolean custom);
}
