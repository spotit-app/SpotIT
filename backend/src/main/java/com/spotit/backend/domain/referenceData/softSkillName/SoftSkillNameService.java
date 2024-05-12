package com.spotit.backend.domain.referenceData.softSkillName;

import java.util.Optional;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataService;

public interface SoftSkillNameService extends AbstractReferenceDataService<SoftSkillName, Integer> {

    Optional<SoftSkillName> getByName(String name);
}
