package com.spotit.backend.techSkill.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteTechSkillNameDto(
        String name,
        String logoUrl) implements WriteDto {
}
