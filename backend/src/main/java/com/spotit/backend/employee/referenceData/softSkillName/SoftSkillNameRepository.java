package com.spotit.backend.employee.referenceData.softSkillName;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftSkillNameRepository extends JpaRepository<SoftSkillName, Integer> {

    List<SoftSkillName> findByCustom(Boolean custom);

    Optional<SoftSkillName> findByName(String name);
}
