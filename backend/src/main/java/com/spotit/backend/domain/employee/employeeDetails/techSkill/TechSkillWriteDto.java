package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record TechSkillWriteDto(
        Integer skillLevel,
        String techSkillName) implements WriteDto {
}
