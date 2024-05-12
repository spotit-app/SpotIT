package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record TechSkillReadDto(
        Integer id,
        Integer skillLevel,
        String techSkillName,
        String logoUrl) implements ReadDto {
}
