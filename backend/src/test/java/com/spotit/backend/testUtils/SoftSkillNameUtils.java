package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.softSkill.dto.ReadSoftSkillNameDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillNameDto;
import com.spotit.backend.softSkill.model.SoftSkillName;

public class SoftSkillNameUtils {

    public static SoftSkillName createSoftSkillName(int id) {
        return SoftSkillName.builder()
                .name("SoftSkillName " + id)
                .custom(false)
                .build();
    }

    public static ReadSoftSkillNameDto createReadSoftSkillNameDto(int id) {
        return ReadSoftSkillNameDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    public static WriteSoftSkillNameDto createWriteSoftSkillNameDto(int id) {
        return WriteSoftSkillNameDto.builder()
                .name("SoftSkillName " + id)
                .build();
    }

    public static List<SoftSkillName> generateSoftSkillNameList(int length) {
        return IntStream.range(0, length)
                .mapToObj(SoftSkillNameUtils::createSoftSkillName)
                .toList();
    }
}
