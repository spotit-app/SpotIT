package com.spotit.backend.domain.referenceData.workExperience;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface WorkExperienceMapper
        extends EntityMapper<WorkExperience, WorkExperienceReadDto, WorkExperienceWriteDto> {

    @Override
    WorkExperienceReadDto toReadDto(WorkExperience workExperience);

    @Override
    @Mapping(target = "id", ignore = true)
    WorkExperience fromWriteDto(WorkExperienceWriteDto writeDto);
}
