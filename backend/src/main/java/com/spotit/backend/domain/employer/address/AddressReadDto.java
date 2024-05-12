package com.spotit.backend.domain.employer.address;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record AddressReadDto(
        Integer id,
        String country,
        String zipCode,
        String city,
        String street) implements ReadDto {
}
