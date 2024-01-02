package com.spotit.backend.employee.referenceData.techSkillName;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record TechSkillNameWriteDto(
        String name,
        String logoUrl) implements WriteDto {
}
