package com.spotit.backend.softSkill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotit.backend.softSkill.model.SoftSkill;

@Repository
public interface SoftSkillRepository extends JpaRepository<SoftSkill, Integer> {

    List<SoftSkill> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
