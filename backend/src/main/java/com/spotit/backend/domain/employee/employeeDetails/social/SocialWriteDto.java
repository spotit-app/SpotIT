package com.spotit.backend.domain.employee.employeeDetails.social;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SocialWriteDto(
        String name,
        String socialUrl) implements WriteDto {
}
