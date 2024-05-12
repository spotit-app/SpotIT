package com.spotit.backend.domain.referenceData.foreignLanguageName;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ForeignLanguageNameWriteDto(
        String name,
        String flagUrl) implements WriteDto {
}
