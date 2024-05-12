package com.spotit.backend.domain.employer.address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface AddressMapper extends EntityMapper<Address, AddressReadDto, AddressWriteDto> {

    @Override
    AddressReadDto toReadDto(Address address);

    @Override
    @Mapping(target = "id", ignore = true)
    Address fromWriteDto(AddressWriteDto addressWriteDto);
}
