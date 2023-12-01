package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageDto;
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageDto;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;

public interface ForeignLanguageUtils {

    static ForeignLanguage createForeignLanguage(Integer id) {
        return ForeignLanguage.builder()
                .languageLevel("Level" + id)
                .build();
    }

    static ReadForeignLanguageDto createReadForeignLanguageDto(Integer id) {
        return ReadForeignLanguageDto.builder()
                .foreignLanguageName("Language " + id)
                .languageLevel("Level" + id)
                .flagUrl("flagUrl" + id)
                .build();
    }

    static WriteForeignLanguageDto createWriteForeignLanguageDto(Integer id) {
        return WriteForeignLanguageDto.builder()
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
