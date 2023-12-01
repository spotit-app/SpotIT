package com.spotit.backend.project.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteProjectDto(
        String description,
        String projectUrl) implements WriteDto {
}
