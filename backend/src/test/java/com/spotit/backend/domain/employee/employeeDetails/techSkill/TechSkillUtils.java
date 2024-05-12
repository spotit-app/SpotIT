package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import java.util.List;
import java.util.stream.IntStream;

public interface TechSkillUtils {

    static TechSkill createTechSkill(Integer id) {
        return TechSkill.builder()
                .skillLevel(2)
                .build();
    }

    static TechSkillReadDto createTechSkillReadDto(Integer id) {
        return TechSkillReadDto.builder()
                .techSkillName("TechSkillName " + id)
                .skillLevel(2)
                .build();
    }

    static TechSkillWriteDto createTechSkillWriteDto(int id) {
        return TechSkillWriteDto.builder()
                .skillLevel(7)
                .techSkillName("Name " + id)
                .build();
    }

    static List<TechSkill> generateTechSkillList(int length) {
        return IntStream.range(0, length)
                .mapToObj(TechSkillUtils::createTechSkill)
                .toList();
    }
}
