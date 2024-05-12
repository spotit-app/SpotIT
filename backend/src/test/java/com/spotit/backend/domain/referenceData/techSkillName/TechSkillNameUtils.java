package com.spotit.backend.domain.referenceData.techSkillName;

import java.util.List;
import java.util.stream.IntStream;

public interface TechSkillNameUtils {

    static TechSkillName createTechSkillName(int id) {
        return TechSkillName.builder()
                .name("TechSkillName " + id)
                .logoUrl("https://example.com/logo" + id + ".jpg")
                .custom(false)
                .build();
    }

    static TechSkillNameReadDto createTechSkillNameReadDto(int id) {
        return TechSkillNameReadDto.builder()
                .name("TechSkillName " + id)
                .logoUrl("https://example.com/logo" + id + ".jpg")
                .build();
    }

    static List<TechSkillName> generateTechSkillNameList(int length) {
        return IntStream.range(0, length)
                .mapToObj(TechSkillNameUtils::createTechSkillName)
                .toList();
    }
}
