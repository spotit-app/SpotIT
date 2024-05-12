package com.spotit.backend.domain.referenceData.workExperience;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataServiceImpl;

@Service
public class WorkExperienceServiceImpl
        extends AbstractReferenceDataServiceImpl<WorkExperience, Integer>
        implements WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;

    public WorkExperienceServiceImpl(WorkExperienceRepository workExperienceRepository) {
        super(workExperienceRepository);
        this.workExperienceRepository = workExperienceRepository;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<WorkExperience> getAll() {
        return workExperienceRepository.findAll();
    }
}
