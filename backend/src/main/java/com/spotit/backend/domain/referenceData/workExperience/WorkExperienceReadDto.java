package com.spotit.backend.domain.referenceData.workExperience;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record WorkExperienceReadDto(
        Integer id,
        String name) implements ReadDto {

}
