package com.spotit.backend.domain.referenceData.educationLevel;

import java.util.Optional;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataService;

public interface EducationLevelService extends AbstractReferenceDataService<EducationLevel, Integer> {

    Optional<EducationLevel> getByName(String name);
}
