package com.spotit.backend.techSkill.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.techSkill.dto.ReadTechSkillNameDto;
import com.spotit.backend.techSkill.dto.WriteTechSkillNameDto;
import com.spotit.backend.techSkill.model.TechSkillName;

@Mapper
public interface TechSkillNameMapper
        extends EntityMapper<TechSkillName, ReadTechSkillNameDto, WriteTechSkillNameDto> {

    @Override
    ReadTechSkillNameDto toReadDto(TechSkillName techSkillName);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "custom", ignore = true)
    @Mapping(target = "techSkills", ignore = true)
    TechSkillName fromWriteDto(WriteTechSkillNameDto techSkillName);
}
