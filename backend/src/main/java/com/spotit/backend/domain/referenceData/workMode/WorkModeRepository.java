package com.spotit.backend.domain.referenceData.workMode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkModeRepository extends JpaRepository<WorkMode, Integer> {
}
