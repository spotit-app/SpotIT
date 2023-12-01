package com.spotit.backend.interest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.interest.dto.ReadInterestDto;
import com.spotit.backend.interest.dto.WriteInterestDto;
import com.spotit.backend.interest.model.Interest;

@Mapper
public interface InterestMapper extends EntityMapper<Interest, ReadInterestDto, WriteInterestDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Interest fromWriteDto(WriteInterestDto writeDto);

    @Override
    ReadInterestDto toReadDto(Interest interest);
}
