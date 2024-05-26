package com.spotit.backend.domain.referenceData.workMode;

import java.util.Optional;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataService;

public interface WorkModeService extends AbstractReferenceDataService<WorkMode, Integer> {

    Optional<WorkMode> getByName(String name);
}
