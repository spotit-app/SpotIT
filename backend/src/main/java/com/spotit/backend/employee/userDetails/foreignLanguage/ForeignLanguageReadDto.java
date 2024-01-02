package com.spotit.backend.employee.userDetails.foreignLanguage;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ForeignLanguageReadDto(
        Integer id,
        String languageLevel,
        String foreignLanguageName,
        String flagUrl) implements ReadDto {
}
