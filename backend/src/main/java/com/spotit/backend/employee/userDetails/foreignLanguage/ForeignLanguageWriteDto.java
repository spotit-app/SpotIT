package com.spotit.backend.employee.userDetails.foreignLanguage;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ForeignLanguageWriteDto(
        String languageLevel,
        Integer foreignLanguageNameId) implements WriteDto {
}
