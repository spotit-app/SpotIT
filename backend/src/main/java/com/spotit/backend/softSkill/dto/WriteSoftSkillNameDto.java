package com.spotit.backend.softSkill.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteSoftSkillNameDto(
        String name) implements WriteDto {

}
