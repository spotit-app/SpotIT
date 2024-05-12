package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.EntityMapper;

@Mapper
public interface ForeignLanguageMapper
        extends EntityMapper<ForeignLanguage, ForeignLanguageReadDto, ForeignLanguageWriteDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "foreignLanguageName", ignore = true)
    ForeignLanguage fromWriteDto(ForeignLanguageWriteDto writeDto);

    @Override
    @Mapping(target = "foreignLanguageName", expression = "java(foreignLanguage.getForeignLanguageName().getName())")
    @Mapping(target = "flagUrl", expression = "java(foreignLanguage.getForeignLanguageName().getFlagUrl())")
    ForeignLanguageReadDto toReadDto(ForeignLanguage foreignLanguage);
}
