package com.spotit.backend.softSkill.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadSoftSkillDto(
        Integer id,
        Integer skillLevel,
        String softSkillName) implements ReadDto {

}
