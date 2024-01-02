package com.spotit.backend.employee.referenceData.softSkillName;

import java.util.Optional;

import com.spotit.backend.employee.referenceData.abstraction.AbstractReferenceDataService;

public interface SoftSkillNameService extends AbstractReferenceDataService<SoftSkillName, Integer> {

    Optional<SoftSkillName> getByName(String name);
}
