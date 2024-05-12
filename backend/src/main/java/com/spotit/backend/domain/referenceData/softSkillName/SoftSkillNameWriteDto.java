package com.spotit.backend.domain.referenceData.softSkillName;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SoftSkillNameWriteDto(
        String name) implements WriteDto {

}
