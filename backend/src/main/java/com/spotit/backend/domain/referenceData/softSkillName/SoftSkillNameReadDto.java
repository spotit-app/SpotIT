package com.spotit.backend.domain.referenceData.softSkillName;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SoftSkillNameReadDto(
        Integer id,
        String name) implements ReadDto {

}
