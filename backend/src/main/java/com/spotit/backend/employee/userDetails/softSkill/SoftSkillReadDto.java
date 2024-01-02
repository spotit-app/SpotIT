package com.spotit.backend.employee.userDetails.softSkill;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SoftSkillReadDto(
        Integer id,
        Integer skillLevel,
        String softSkillName) implements ReadDto {

}
