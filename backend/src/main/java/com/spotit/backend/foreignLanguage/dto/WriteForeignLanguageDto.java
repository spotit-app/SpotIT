package com.spotit.backend.foreignLanguage.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteForeignLanguageDto(
        String languageLevel,
        Integer foreignLanguageNameId) implements WriteDto {
}
