package com.spotit.backend.domain.referenceData.workExperience;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record WorkExperienceWriteDto(
        String name) implements WriteDto {
}
