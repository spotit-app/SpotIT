package com.spotit.backend.domain.employer.company;

import com.spotit.backend.abstraction.WriteDto;
import com.spotit.backend.domain.employer.address.AddressWriteDto;

import lombok.Builder;

@Builder
public record CompanyWriteDto(
        String name,
        String nip,
        String regon,
        String websiteUrl,
        AddressWriteDto address) implements WriteDto {
}
