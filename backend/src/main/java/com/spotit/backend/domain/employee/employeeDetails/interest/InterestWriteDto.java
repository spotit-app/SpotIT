package com.spotit.backend.domain.employee.employeeDetails.interest;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record InterestWriteDto(String name) implements WriteDto {
}
