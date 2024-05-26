package com.spotit.backend.domain.referenceData.workExperience;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Integer> {

    Optional<WorkExperience> findByName(String name);
}
