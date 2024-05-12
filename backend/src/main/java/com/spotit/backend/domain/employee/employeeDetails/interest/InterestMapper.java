package com.spotit.backend.domain.employee.employeeDetails.interest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface InterestMapper extends EntityMapper<Interest, InterestReadDto, InterestWriteDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Interest fromWriteDto(InterestWriteDto writeDto);

    @Override
    InterestReadDto toReadDto(Interest interest);
}
