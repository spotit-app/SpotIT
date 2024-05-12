package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SoftSkillReadDto(
        Integer id,
        Integer skillLevel,
        String softSkillName) implements ReadDto {

}
