package com.spotit.backend.domain.employer.company;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employer.address.AddressReadDto;

import lombok.Builder;

@Builder
public record CompanyReadDto(
        Integer id,
        String name,
        String nip,
        String regon,
        String websiteUrl,
        String logoUrl,
        AddressReadDto address) implements ReadDto {
}
