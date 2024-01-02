package com.spotit.backend.employee.userDetails.softSkill;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SoftSkillWriteDto(
        Integer skillLevel,
        String softSkillName) implements WriteDto {

}
