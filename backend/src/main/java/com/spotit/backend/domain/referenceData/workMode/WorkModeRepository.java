package com.spotit.backend.domain.referenceData.workMode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkModeRepository extends JpaRepository<WorkMode, Integer> {

    Optional<WorkMode> findByName(String name);
}
