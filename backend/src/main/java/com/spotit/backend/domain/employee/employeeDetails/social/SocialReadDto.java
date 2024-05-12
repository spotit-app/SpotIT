package com.spotit.backend.domain.employee.employeeDetails.social;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SocialReadDto(
        Integer id,
        String name,
        String socialUrl) implements ReadDto {
}
