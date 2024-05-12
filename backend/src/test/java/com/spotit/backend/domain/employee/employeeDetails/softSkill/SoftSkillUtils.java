package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;

public interface SoftSkillUtils {

    static SoftSkill createSoftSkill(Integer id) {
        SoftSkillName softSkillName = createSoftSkillName(id);
        return SoftSkill.builder()
                .id(id)
                .softSkillName(softSkillName)
                .skillLevel(2)
                .build();
    }

    static SoftSkillReadDto createSoftSkillReadDto(Integer id) {
        return SoftSkillReadDto.builder()
                .id(id)
                .softSkillName("SoftSkillName " + id)
                .skillLevel(2)
                .build();
    }

    static SoftSkillWriteDto createSoftSkillWriteDto(int id) {
        return SoftSkillWriteDto.builder()
                .skillLevel(7)
                .softSkillName("Name " + id)
                .build();
    }

    static List<SoftSkill> generateSoftSkillList(int length) {
        return IntStream.range(0, length)
                .mapToObj(SoftSkillUtils::createSoftSkill)
                .toList();
    }
}
