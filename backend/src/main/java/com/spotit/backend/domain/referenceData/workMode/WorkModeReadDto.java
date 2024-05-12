package com.spotit.backend.domain.referenceData.workMode;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record WorkModeReadDto(
        Integer id,
        String name) implements ReadDto {

}
