package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageNameDto;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;

public interface ForeignLanguageNameUtils {

    static ForeignLanguageName createForeignLanguageName(Integer id) {
        return ForeignLanguageName.builder()
                .name("Name " + id)
                .flagUrl("flagUrl" + id)
                .build();
    }

    static List<ForeignLanguageName> generateForeignLanguageNameList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(ForeignLanguageNameUtils::createForeignLanguageName)
                .toList();
    }

    static ReadForeignLanguageNameDto createReadForeignLanguageNameDto(Integer id) {
        return ReadForeignLanguageNameDto.builder()
                .name("Name " + id)
                .flagUrl("flagUrl" + id)
                .build();
    }
}
