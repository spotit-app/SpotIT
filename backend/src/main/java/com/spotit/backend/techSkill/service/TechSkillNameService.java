package com.spotit.backend.techSkill.service;

import java.util.List;
import java.util.Optional;

import com.spotit.backend.abstraction.service.AbstractService;
import com.spotit.backend.techSkill.model.TechSkillName;

public interface TechSkillNameService extends AbstractService<TechSkillName, Integer> {

    List<TechSkillName> getAll();

    Optional<TechSkillName> getByName(String name);

    TechSkillName create(String name, byte[] logo);

    TechSkillName update(Integer id, String name, byte[] logo);
}
