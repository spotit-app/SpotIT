package com.spotit.backend.techSkill.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteTechSkillDto(
        Integer skillLevel,
        String techSkillName) implements WriteDto {
}
