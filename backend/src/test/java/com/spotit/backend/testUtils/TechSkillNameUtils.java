package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.techSkill.dto.ReadTechSkillNameDto;
import com.spotit.backend.techSkill.model.TechSkillName;

public class TechSkillNameUtils {

    public static TechSkillName createTechSkillName(int id) {
        return TechSkillName.builder()
                .name("TechSkillName " + id)
                .logoUrl("https://example.com/logo" + id + ".jpg")
                .custom(false)
                .build();
    }

    public static ReadTechSkillNameDto createReadTechSkillNameDto(int id) {
        return ReadTechSkillNameDto.builder()
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
