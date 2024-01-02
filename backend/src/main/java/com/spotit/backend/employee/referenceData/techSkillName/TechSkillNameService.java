package com.spotit.backend.employee.referenceData.techSkillName;

import java.util.Optional;

import com.spotit.backend.employee.referenceData.abstraction.AbstractReferenceDataService;

public interface TechSkillNameService extends AbstractReferenceDataService<TechSkillName, Integer> {

    Optional<TechSkillName> getByName(String name);

    TechSkillName create(String name, byte[] logo);

    TechSkillName update(Integer id, String name, byte[] logo);
}
