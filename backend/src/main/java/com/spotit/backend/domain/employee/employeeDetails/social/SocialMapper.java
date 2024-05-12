package com.spotit.backend.domain.employee.employeeDetails.social;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface SocialMapper extends EntityMapper<Social, SocialReadDto, SocialWriteDto> {

    @Override
    SocialReadDto toReadDto(Social social);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Social fromWriteDto(SocialWriteDto writeSocialDto);
}
