package com.spotit.backend.domain.referenceData.techSkillName;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface TechSkillNameMapper
        extends EntityMapper<TechSkillName, TechSkillNameReadDto, TechSkillNameWriteDto> {

    @Override
    TechSkillNameReadDto toReadDto(TechSkillName techSkillName);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "custom", ignore = true)
    @Mapping(target = "techSkills", ignore = true)
    TechSkillName fromWriteDto(TechSkillNameWriteDto techSkillName);
}
