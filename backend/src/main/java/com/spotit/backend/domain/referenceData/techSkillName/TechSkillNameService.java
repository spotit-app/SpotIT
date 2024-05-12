package com.spotit.backend.domain.referenceData.techSkillName;

import java.util.Optional;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataService;

public interface TechSkillNameService extends AbstractReferenceDataService<TechSkillName, Integer> {

    Optional<TechSkillName> getByName(String name);

    TechSkillName create(String name, byte[] logo);

    TechSkillName update(Integer id, String name, byte[] logo);
}
