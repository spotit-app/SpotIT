package com.spotit.backend.education.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.education.dto.EducationDto;
import com.spotit.backend.education.model.Education;

@Mapper
public interface EducationMapper {

    @Mapping(target = "educationLevelName", expression = "java(education.getEducationLevel().getName())")
    EducationDto toDto(Education education);
}
