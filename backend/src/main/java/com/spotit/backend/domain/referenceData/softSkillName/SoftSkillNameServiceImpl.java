package com.spotit.backend.domain.referenceData.softSkillName;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataServiceImpl;

@Service
public class SoftSkillNameServiceImpl
        extends AbstractReferenceDataServiceImpl<SoftSkillName, Integer>
        implements SoftSkillNameService {

    private final SoftSkillNameRepository softSkillNameRepository;

    public SoftSkillNameServiceImpl(SoftSkillNameRepository softSkillNameRepository) {
        super(softSkillNameRepository);
        this.softSkillNameRepository = softSkillNameRepository;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<SoftSkillName> getAll() {
        return softSkillNameRepository.findByCustom(false);
    }

    @Override
    public Optional<SoftSkillName> getByName(String name) {
        return softSkillNameRepository.findByName(name);
    }
}
