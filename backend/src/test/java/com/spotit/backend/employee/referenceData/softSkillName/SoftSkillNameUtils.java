package com.spotit.backend.employee.referenceData.softSkillName;

import java.util.List;
import java.util.stream.IntStream;

public class SoftSkillNameUtils {

    public static SoftSkillName createSoftSkillName(int id) {
        return SoftSkillName.builder()
                .name("SoftSkillName " + id)
                .custom(false)
                .build();
    }

    public static SoftSkillNameReadDto createSoftSkillNameReadDto(int id) {
        return SoftSkillNameReadDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    public static SoftSkillNameWriteDto createSoftSkillNameWriteDto(int id) {
        return SoftSkillNameWriteDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    public static List<SoftSkillName> generateSoftSkillNameList(int length) {
        return IntStream.range(0, length)
                .mapToObj(SoftSkillNameUtils::createSoftSkillName)
                .toList();
    }
}
