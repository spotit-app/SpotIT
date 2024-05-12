package com.spotit.backend.domain.referenceData.techSkillName;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record TechSkillNameReadDto(
        Integer id,
        String name,
        String logoUrl) implements ReadDto {
}
