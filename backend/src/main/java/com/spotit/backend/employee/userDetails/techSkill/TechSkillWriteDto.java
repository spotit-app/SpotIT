package com.spotit.backend.employee.userDetails.techSkill;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record TechSkillWriteDto(
        Integer skillLevel,
        String techSkillName) implements WriteDto {
}
