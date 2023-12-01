package com.spotit.backend.techSkill.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadTechSkillDto(
        Integer id,
        Integer skillLevel,
        String techSkillName) implements ReadDto {
}
