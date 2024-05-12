package com.spotit.backend.domain.referenceData.educationLevel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Integer> {

    List<EducationLevel> findByCustom(Boolean custom);

    Optional<EducationLevel> findByName(String name);
}
