package com.spotit.backend.employee.referenceData.educationLevel;

import java.util.Optional;

import com.spotit.backend.employee.referenceData.abstraction.AbstractReferenceDataService;

public interface EducationLevelService extends AbstractReferenceDataService<EducationLevel, Integer> {

    Optional<EducationLevel> getByName(String name);
}
