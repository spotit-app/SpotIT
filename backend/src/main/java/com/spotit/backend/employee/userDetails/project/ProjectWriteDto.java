package com.spotit.backend.employee.userDetails.project;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ProjectWriteDto(
        String name,
        String description,
        String projectUrl) implements WriteDto {
}
