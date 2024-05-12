package com.spotit.backend.domain.employer.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;
import com.spotit.backend.domain.employer.address.AddressMapper;

@Mapper(uses = {
        AddressMapper.class
})
public interface CompanyMapper extends EntityMapper<Company, CompanyReadDto, CompanyWriteDto> {

    @Override
    CompanyReadDto toReadDto(Company company);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    Company fromWriteDto(CompanyWriteDto companyWriteDto);
}
