package com.spotit.backend.techSkill.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadTechSkillNameDto(
        Integer id,
        String name,
        String logoUrl) implements ReadDto {
}
