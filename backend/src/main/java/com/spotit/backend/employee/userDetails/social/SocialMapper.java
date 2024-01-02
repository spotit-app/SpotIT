package com.spotit.backend.employee.userDetails.social;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface SocialMapper extends EntityMapper<Social, SocialReadDto, SocialWriteDto> {

    @Override
    SocialReadDto toReadDto(Social social);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Social fromWriteDto(SocialWriteDto writeSocialDto);
}
