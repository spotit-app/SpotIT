package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface SoftSkillMapper extends EntityMapper<SoftSkill, SoftSkillReadDto, SoftSkillWriteDto> {

    @Override
    @Mapping(target = "softSkillName", expression = "java(softSkill.getSoftSkillName().getName())")
    SoftSkillReadDto toReadDto(SoftSkill softSkill);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "softSkillName", ignore = true)
    SoftSkill fromWriteDto(SoftSkillWriteDto writeDto);
}
