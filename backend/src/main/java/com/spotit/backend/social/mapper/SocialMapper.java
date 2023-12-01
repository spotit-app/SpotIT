package com.spotit.backend.social.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.social.dto.ReadSocialDto;
import com.spotit.backend.social.dto.WriteSocialDto;
import com.spotit.backend.social.model.Social;

@Mapper
public interface SocialMapper extends EntityMapper<Social, ReadSocialDto, WriteSocialDto> {

    @Override
    ReadSocialDto toReadDto(Social social);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Social fromWriteDto(WriteSocialDto writeSocialDto);
}
