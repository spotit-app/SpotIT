package com.spotit.backend.employee.referenceData.techSkillName;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record TechSkillNameReadDto(
        Integer id,
        String name,
        String logoUrl) implements ReadDto {
}
