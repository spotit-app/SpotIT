package com.spotit.backend.domain.employee.employeeDetails.project;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ProjectWriteDto(
        String name,
        String description,
        String projectUrl) implements WriteDto {
}
