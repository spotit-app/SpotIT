package com.spotit.backend.techSkill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotit.backend.techSkill.model.TechSkill;

@Repository
public interface TechSkillRepository extends JpaRepository<TechSkill, Integer> {

    List<TechSkill> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
