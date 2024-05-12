package com.spotit.backend.domain.employee.employeeDetails.project;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ProjectReadDto(
        Integer id,
        String name,
        String description,
        String projectUrl) implements ReadDto {
}
