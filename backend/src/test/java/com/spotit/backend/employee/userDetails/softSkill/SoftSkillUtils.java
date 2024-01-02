package com.spotit.backend.employee.userDetails.softSkill;

import static com.spotit.backend.employee.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.employee.referenceData.softSkillName.SoftSkillName;

public class SoftSkillUtils {

    public static SoftSkill createSoftSkill(Integer id) {
        SoftSkillName softSkillName = createSoftSkillName(id);
        SoftSkill softSkill = SoftSkill.builder()
                .id(id)
                .softSkillName(softSkillName)
                .skillLevel(2)
                .build();
        return softSkill;
    }

    public static SoftSkillReadDto createSoftSkillReadDto(Integer id) {
        return SoftSkillReadDto.builder()
                .id(id)
                .softSkillName("SoftSkillName " + id)
                .skillLevel(2)
                .build();
    }

    public static SoftSkillWriteDto createSoftSkillWriteDto(int id) {
        return SoftSkillWriteDto.builder()
                .skillLevel(7)
                .softSkillName("Name " + id)
                .build();
    }

    public static List<SoftSkill> generateSoftSkillList(int length) {
        return IntStream.range(0, length)
                .mapToObj(SoftSkillUtils::createSoftSkill)
                .toList();
    }
}
