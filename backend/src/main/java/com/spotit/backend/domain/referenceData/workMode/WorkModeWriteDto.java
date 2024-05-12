package com.spotit.backend.domain.referenceData.workMode;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record WorkModeWriteDto(
        String name) implements WriteDto {
}
