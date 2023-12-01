package com.spotit.backend.foreignLanguage.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadForeignLanguageNameDto(
        Integer id,
        String name,
        String flagUrl) implements ReadDto {
}
