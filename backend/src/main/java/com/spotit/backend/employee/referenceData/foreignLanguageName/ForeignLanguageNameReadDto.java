package com.spotit.backend.employee.referenceData.foreignLanguageName;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ForeignLanguageNameReadDto(
        Integer id,
        String name,
        String flagUrl) implements ReadDto {
}
