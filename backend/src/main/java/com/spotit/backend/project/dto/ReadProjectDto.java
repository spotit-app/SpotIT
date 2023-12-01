package com.spotit.backend.project.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadProjectDto(
        Integer id,
        String description,
        String projectUrl) implements ReadDto {
}
