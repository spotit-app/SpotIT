package com.spotit.backend.softSkill.service;

import java.util.List;
import java.util.Optional;

import com.spotit.backend.abstraction.service.AbstractService;
import com.spotit.backend.softSkill.model.SoftSkillName;

public interface SoftSkillNameService extends AbstractService<SoftSkillName, Integer> {

    List<SoftSkillName> getAll();

    Optional<SoftSkillName> getByName(String name);
}
