package com.spotit.backend.techSkill.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.techSkill.dto.ReadTechSkillDto;
import com.spotit.backend.techSkill.dto.WriteTechSkillDto;
import com.spotit.backend.techSkill.model.TechSkill;

@Mapper
public interface TechSkillMapper extends EntityMapper<TechSkill, ReadTechSkillDto, WriteTechSkillDto> {

    @Override
    @Mapping(target = "techSkillName", expression = "java(techSkill.getTechSkillName().getName())")
    ReadTechSkillDto toReadDto(TechSkill techSkill);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "techSkillName", ignore = true)
    TechSkill fromWriteDto(WriteTechSkillDto writeDto);
}
