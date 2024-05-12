package com.spotit.backend.domain.referenceData.techSkillName;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record TechSkillNameWriteDto(
        String name,
        String logoUrl) implements WriteDto {
}
