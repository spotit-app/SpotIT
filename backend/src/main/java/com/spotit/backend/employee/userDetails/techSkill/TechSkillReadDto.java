package com.spotit.backend.employee.userDetails.techSkill;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record TechSkillReadDto(
        Integer id,
        Integer skillLevel,
        String techSkillName,
        String logoUrl) implements ReadDto {
}
