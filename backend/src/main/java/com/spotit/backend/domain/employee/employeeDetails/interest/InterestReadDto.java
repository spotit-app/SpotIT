package com.spotit.backend.domain.employee.employeeDetails.interest;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record InterestReadDto(
        Integer id,
        String name) implements ReadDto {
}
