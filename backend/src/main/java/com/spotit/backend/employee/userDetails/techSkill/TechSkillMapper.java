package com.spotit.backend.employee.userDetails.techSkill;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface TechSkillMapper extends EntityMapper<TechSkill, TechSkillReadDto, TechSkillWriteDto> {

    @Override
    @Mapping(target = "techSkillName", expression = "java(techSkill.getTechSkillName().getName())")
    @Mapping(target = "logoUrl", expression = "java(techSkill.getTechSkillName().getLogoUrl())")
    TechSkillReadDto toReadDto(TechSkill techSkill);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "techSkillName", ignore = true)
    TechSkill fromWriteDto(TechSkillWriteDto writeDto);
}
