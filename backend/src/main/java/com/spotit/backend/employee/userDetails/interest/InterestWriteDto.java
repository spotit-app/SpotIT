package com.spotit.backend.employee.userDetails.interest;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record InterestWriteDto(String name) implements WriteDto {
}
