package com.spotit.backend.foreignLanguage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.abstraction.mapper.EntityMapper;
import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageNameDto;
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageNameDto;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;

@Mapper
public interface ForeignLanguageNameMapper
        extends EntityMapper<ForeignLanguageName, ReadForeignLanguageNameDto, WriteForeignLanguageNameDto> {

    @Override
    ReadForeignLanguageNameDto toReadDto(ForeignLanguageName foreignLanguageName);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foreignLanguages", ignore = true)
    ForeignLanguageName fromWriteDto(WriteForeignLanguageNameDto writeForeignLanguageNameDto);
}
