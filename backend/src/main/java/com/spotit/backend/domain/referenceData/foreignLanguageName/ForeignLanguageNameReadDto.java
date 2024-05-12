package com.spotit.backend.domain.referenceData.foreignLanguageName;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ForeignLanguageNameReadDto(
        Integer id,
        String name,
        String flagUrl) implements ReadDto {
}
