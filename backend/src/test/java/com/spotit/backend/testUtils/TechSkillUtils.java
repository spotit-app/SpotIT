package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.techSkill.dto.ReadTechSkillDto;
import com.spotit.backend.techSkill.dto.WriteTechSkillDto;
import com.spotit.backend.techSkill.model.TechSkill;

public class TechSkillUtils {

    public static TechSkill createTechSkill(Integer id) {
        return TechSkill.builder()
                .skillLevel(2)
                .build();
    }

    public static ReadTechSkillDto createReadTechSkillDto(Integer id) {
        return ReadTechSkillDto.builder()
                .techSkillName("TechSkillName " + id)
                .skillLevel(2)
                .build();
    }

    public static WriteTechSkillDto createWriteTechSkillDto(int id) {
        return WriteTechSkillDto.builder()
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
