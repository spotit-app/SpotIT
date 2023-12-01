package com.spotit.backend.experience.mapper;

import org.mapstruct.Mapper;

import com.spotit.backend.experience.dto.ExperienceDto;
import com.spotit.backend.experience.model.Experience;

@Mapper
public interface ExperienceMapper {

    ExperienceDto toDto(Experience experience);
}
