package com.spotit.backend.softSkill.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.softSkill.dto.ReadSoftSkillDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillDto;
import com.spotit.backend.softSkill.model.SoftSkill;

@Mapper
public interface SoftSkillMapper extends EntityMapper<SoftSkill, ReadSoftSkillDto, WriteSoftSkillDto> {

    @Override
    @Mapping(target = "softSkillName", expression = "java(softSkill.getSoftSkillName().getName())")
    ReadSoftSkillDto toReadDto(SoftSkill softSkill);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "softSkillName", ignore = true)
    SoftSkill fromWriteDto(WriteSoftSkillDto writeDto);
}
