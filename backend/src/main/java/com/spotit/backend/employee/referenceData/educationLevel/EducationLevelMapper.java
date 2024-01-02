package com.spotit.backend.employee.referenceData.educationLevel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface EducationLevelMapper
        extends EntityMapper<EducationLevel, EducationLevelReadDto, EducationLevelWriteDto> {

    @Override
    EducationLevelReadDto toReadDto(EducationLevel educationLevel);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "custom", ignore = true)
    @Mapping(target = "educations", ignore = true)
    EducationLevel fromWriteDto(EducationLevelWriteDto educationLevel);
}
