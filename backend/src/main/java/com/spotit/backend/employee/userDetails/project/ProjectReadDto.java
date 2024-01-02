package com.spotit.backend.employee.userDetails.project;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ProjectReadDto(
        Integer id,
        String name,
        String description,
        String projectUrl) implements ReadDto {
}
