package com.spotit.backend.employee.referenceData.foreignLanguageName;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ForeignLanguageNameWriteDto(
        String name,
        String flagUrl) implements WriteDto {
}
