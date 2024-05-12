package com.spotit.backend.domain.referenceData.foreignLanguageName;

import java.util.List;
import java.util.stream.IntStream;

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

    static ForeignLanguageNameReadDto createForeignLanguageNameReadDto(Integer id) {
        return ForeignLanguageNameReadDto.builder()
                .name("Name " + id)
                .flagUrl("flagUrl" + id)
                .build();
    }
}
