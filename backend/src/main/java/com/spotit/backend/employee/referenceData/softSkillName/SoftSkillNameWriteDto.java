package com.spotit.backend.employee.referenceData.softSkillName;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SoftSkillNameWriteDto(
        String name) implements WriteDto {

}
