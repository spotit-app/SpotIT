package com.spotit.backend.domain.referenceData.educationLevel;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record EducationLevelReadDto(
        Integer id,
        String name) implements ReadDto {

}
