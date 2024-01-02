package com.spotit.backend.employee.userDetails.experience;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface ExperienceMapper extends EntityMapper<Experience, ExperienceReadDto, ExperienceWriteDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Experience fromWriteDto(ExperienceWriteDto writeDto);

    @Override
    ExperienceReadDto toReadDto(Experience experience);
}
