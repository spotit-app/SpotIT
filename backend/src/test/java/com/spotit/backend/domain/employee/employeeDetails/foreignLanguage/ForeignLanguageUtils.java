package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import java.util.List;
import java.util.stream.IntStream;

public interface ForeignLanguageUtils {

    static ForeignLanguage createForeignLanguage(Integer id) {
        return ForeignLanguage.builder()
                .languageLevel("Level" + id)
                .build();
    }

    static ForeignLanguageReadDto createForeignLanguageReadDto(Integer id) {
        return ForeignLanguageReadDto.builder()
                .foreignLanguageName("Language " + id)
                .languageLevel("Level" + id)
                .flagUrl("flagUrl" + id)
                .build();
    }

    static ForeignLanguageWriteDto createForeignLanguageWriteDto(Integer id) {
        return ForeignLanguageWriteDto.builder()
                .languageLevel("Level" + id)
                .foreignLanguageNameId(id)
                .build();
    }

    static List<ForeignLanguage> generateForeignLanguageList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(ForeignLanguageUtils::createForeignLanguage)
                .toList();
    }
}
