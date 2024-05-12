package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ForeignLanguageReadDto(
        Integer id,
        String languageLevel,
        String foreignLanguageName,
        String flagUrl) implements ReadDto {
}
