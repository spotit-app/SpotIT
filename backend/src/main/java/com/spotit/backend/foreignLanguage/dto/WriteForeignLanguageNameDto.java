package com.spotit.backend.foreignLanguage.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteForeignLanguageNameDto(
        String name,
        String flagUrl) implements WriteDto {
}
