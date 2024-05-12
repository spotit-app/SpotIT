package com.spotit.backend.domain.employer.address;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record AddressWriteDto(
        String country,
        String zipCode,
        String city,
        String street) implements WriteDto {
}
