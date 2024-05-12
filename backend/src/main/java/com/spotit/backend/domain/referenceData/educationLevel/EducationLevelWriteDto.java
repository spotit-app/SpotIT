package com.spotit.backend.domain.referenceData.educationLevel;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record EducationLevelWriteDto(
        String name) implements WriteDto {

}
