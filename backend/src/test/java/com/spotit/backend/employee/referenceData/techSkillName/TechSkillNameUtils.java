package com.spotit.backend.employee.referenceData.techSkillName;

import java.util.List;
import java.util.stream.IntStream;

public class TechSkillNameUtils {

    public static TechSkillName createTechSkillName(int id) {
        return TechSkillName.builder()
                .name("TechSkillName " + id)
                .logoUrl("https://example.com/logo" + id + ".jpg")
                .custom(false)
                .build();
    }

    public static TechSkillNameReadDto createTechSkillNameReadDto(int id) {
        return TechSkillNameReadDto.builder()
                .name("TechSkillName " + id)
                .logoUrl("https://example.com/logo" + id + ".jpg")
                .build();
    }

    public static List<TechSkillName> generateTechSkillNameList(int length) {
        return IntStream.range(0, length)
                .mapToObj(TechSkillNameUtils::createTechSkillName)
                .toList();
    }
}
