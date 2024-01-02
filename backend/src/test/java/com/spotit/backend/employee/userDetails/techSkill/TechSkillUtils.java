package com.spotit.backend.employee.userDetails.techSkill;

import java.util.List;
import java.util.stream.IntStream;

public class TechSkillUtils {

    public static TechSkill createTechSkill(Integer id) {
        return TechSkill.builder()
                .skillLevel(2)
                .build();
    }

    public static TechSkillReadDto createTechSkillReadDto(Integer id) {
        return TechSkillReadDto.builder()
                .techSkillName("TechSkillName " + id)
                .skillLevel(2)
                .build();
    }

    public static TechSkillWriteDto createTechSkillWriteDto(int id) {
        return TechSkillWriteDto.builder()
                .skillLevel(7)
                .techSkillName("Name " + id)
                .build();
    }

    public static List<TechSkill> generateTechSkillList(int length) {
        return IntStream.range(0, length)
                .mapToObj(TechSkillUtils::createTechSkill)
                .toList();
    }
}
