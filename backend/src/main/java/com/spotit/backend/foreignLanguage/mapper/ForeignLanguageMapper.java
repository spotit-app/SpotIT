package com.spotit.backend.foreignLanguage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageDto;
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageDto;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;

@Mapper
public interface ForeignLanguageMapper
        extends EntityMapper<ForeignLanguage, ReadForeignLanguageDto, WriteForeignLanguageDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "foreignLanguageName", ignore = true)
    ForeignLanguage fromWriteDto(WriteForeignLanguageDto writeDto);

    @Override
    @Mapping(target = "foreignLanguageName", expression = "java(foreignLanguage.getForeignLanguageName().getName())")
    @Mapping(target = "flagUrl", expression = "java(foreignLanguage.getForeignLanguageName().getFlagUrl())")
    ReadForeignLanguageDto toReadDto(ForeignLanguage foreignLanguage);
}
