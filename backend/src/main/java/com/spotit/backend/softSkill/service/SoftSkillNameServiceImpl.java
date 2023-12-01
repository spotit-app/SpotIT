package com.spotit.backend.softSkill.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.softSkill.model.SoftSkillName;
import com.spotit.backend.softSkill.repository.SoftSkillNameRepository;

@Service
public class SoftSkillNameServiceImpl
        extends AbstractServiceImpl<SoftSkillName, Integer>
        implements SoftSkillNameService {

    private final SoftSkillNameRepository softSkillNameRepository;

    public SoftSkillNameServiceImpl(SoftSkillNameRepository softSkillNameRepository) {
        super(softSkillNameRepository);
        this.softSkillNameRepository = softSkillNameRepository;
    }

    @Override
    public List<SoftSkillName> getAll() {
        return softSkillNameRepository.findByCustom(false);
    }

    @Override
    public Optional<SoftSkillName> getByName(String name) {
        return softSkillNameRepository.findByName(name);
    }
}
