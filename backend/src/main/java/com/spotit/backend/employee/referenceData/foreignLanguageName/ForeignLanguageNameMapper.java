package com.spotit.backend.employee.referenceData.foreignLanguageName;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface ForeignLanguageNameMapper
        extends EntityMapper<ForeignLanguageName, ForeignLanguageNameReadDto, ForeignLanguageNameWriteDto> {

    @Override
    ForeignLanguageNameReadDto toReadDto(ForeignLanguageName foreignLanguageName);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foreignLanguages", ignore = true)
    ForeignLanguageName fromWriteDto(ForeignLanguageNameWriteDto writeForeignLanguageNameDto);
}
