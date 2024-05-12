package com.spotit.backend.domain.referenceData.softSkillName;

import java.util.List;
import java.util.stream.IntStream;

public interface SoftSkillNameUtils {

    static SoftSkillName createSoftSkillName(int id) {
        return SoftSkillName.builder()
                .name("SoftSkillName " + id)
                .custom(false)
                .build();
    }

    static SoftSkillNameReadDto createSoftSkillNameReadDto(int id) {
        return SoftSkillNameReadDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    static SoftSkillNameWriteDto createSoftSkillNameWriteDto(int id) {
        return SoftSkillNameWriteDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    static List<SoftSkillName> generateSoftSkillNameList(int length) {
        return IntStream.range(0, length)
                .mapToObj(SoftSkillNameUtils::createSoftSkillName)
                .toList();
    }
}
