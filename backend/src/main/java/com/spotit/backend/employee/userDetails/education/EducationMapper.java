package com.spotit.backend.employee.userDetails.education;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface EducationMapper
        extends EntityMapper<Education, EducationReadDto, EducationWriteDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "educationLevel", ignore = true)
    Education fromWriteDto(EducationWriteDto writeDto);

    @Override
    @Mapping(target = "educationLevel", expression = "java(education.getEducationLevel().getName())")
    EducationReadDto toReadDto(Education education);
}
