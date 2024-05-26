package com.spotit.backend.domain.referenceData.workMode;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataServiceImpl;

@Service
public class WorkModeServiceImpl
        extends AbstractReferenceDataServiceImpl<WorkMode, Integer>
        implements WorkModeService {

    private final WorkModeRepository workModeRepository;

    public WorkModeServiceImpl(WorkModeRepository workModeRepository) {
        super(workModeRepository);
        this.workModeRepository = workModeRepository;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<WorkMode> getAll() {
        return workModeRepository.findAll();
    }

    @Override
    public Optional<WorkMode> getByName(String name) {
        return workModeRepository.findByName(name);
    }
}
