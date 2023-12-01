package com.spotit.backend.foreignLanguage.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadForeignLanguageDto(
        Integer id,
        String languageLevel,
        String foreignLanguageName,
        String flagUrl) implements ReadDto {
}
