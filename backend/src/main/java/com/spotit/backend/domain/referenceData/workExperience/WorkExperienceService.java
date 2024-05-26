package com.spotit.backend.domain.referenceData.workExperience;

import java.util.Optional;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataService;

public interface WorkExperienceService extends AbstractReferenceDataService<WorkExperience, Integer> {

    Optional<WorkExperience> getByName(String name);
}
