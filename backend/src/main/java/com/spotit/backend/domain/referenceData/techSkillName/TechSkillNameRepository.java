package com.spotit.backend.domain.referenceData.techSkillName;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechSkillNameRepository extends JpaRepository<TechSkillName, Integer> {

    Optional<TechSkillName> findByName(String name);

    List<TechSkillName> findByCustom(Boolean custom);
}
