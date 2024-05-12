package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ForeignLanguageWriteDto(
        String languageLevel,
        Integer foreignLanguageNameId) implements WriteDto {
}
