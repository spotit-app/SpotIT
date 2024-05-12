package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SoftSkillWriteDto(
        Integer skillLevel,
        String softSkillName) implements WriteDto {

}
