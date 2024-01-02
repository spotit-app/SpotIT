package com.spotit.backend.employee.userDetails.interest;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record InterestReadDto(
        Integer id,
        String name) implements ReadDto {
}
