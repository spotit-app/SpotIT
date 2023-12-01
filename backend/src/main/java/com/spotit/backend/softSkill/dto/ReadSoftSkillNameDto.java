package com.spotit.backend.softSkill.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadSoftSkillNameDto(
        Integer id,
        String name) implements ReadDto {

}
