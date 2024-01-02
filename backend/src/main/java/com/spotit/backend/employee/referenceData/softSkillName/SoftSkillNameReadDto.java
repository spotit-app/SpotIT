package com.spotit.backend.employee.referenceData.softSkillName;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SoftSkillNameReadDto(
        Integer id,
        String name) implements ReadDto {

}
