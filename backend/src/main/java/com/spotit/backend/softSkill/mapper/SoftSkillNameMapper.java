package com.spotit.backend.softSkill.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.softSkill.dto.ReadSoftSkillNameDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillNameDto;
import com.spotit.backend.softSkill.model.SoftSkillName;

@Mapper
public interface SoftSkillNameMapper
        extends EntityMapper<SoftSkillName, ReadSoftSkillNameDto, WriteSoftSkillNameDto> {

    @Override
    ReadSoftSkillNameDto toReadDto(SoftSkillName softSkillName);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "custom", ignore = true)
    @Mapping(target = "softSkills", ignore = true)
    SoftSkillName fromWriteDto(WriteSoftSkillNameDto softSkillName);
}
