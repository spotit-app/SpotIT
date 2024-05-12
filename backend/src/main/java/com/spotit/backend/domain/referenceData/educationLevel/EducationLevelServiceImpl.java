package com.spotit.backend.domain.referenceData.educationLevel;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataServiceImpl;

@Service
public class EducationLevelServiceImpl
        extends AbstractReferenceDataServiceImpl<EducationLevel, Integer>
        implements EducationLevelService {

    private final EducationLevelRepository educationLevelRepository;

    public EducationLevelServiceImpl(EducationLevelRepository educationLevelRepository) {
        super(educationLevelRepository);
        this.educationLevelRepository = educationLevelRepository;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<EducationLevel> getAll() {
        return educationLevelRepository.findByCustom(false);
    }

    @Override
    public Optional<EducationLevel> getByName(String name) {
        return educationLevelRepository.findByName(name);
    }
}
