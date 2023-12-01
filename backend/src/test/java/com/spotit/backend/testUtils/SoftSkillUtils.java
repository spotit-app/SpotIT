package com.spotit.backend.testUtils;

import static com.spotit.backend.testUtils.SoftSkillNameUtils.createSoftSkillName;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.softSkill.dto.ReadSoftSkillDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillDto;
import com.spotit.backend.softSkill.model.SoftSkill;
import com.spotit.backend.softSkill.model.SoftSkillName;

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

    public static ReadSoftSkillDto createReadSoftSkillDto(Integer id) {
        return ReadSoftSkillDto.builder()
                .id(id)
                .softSkillName("SoftSkillName " + id)
                .skillLevel(2)
                .build();
    }

    public static WriteSoftSkillDto createWriteSoftSkillDto(int id) {
        return WriteSoftSkillDto.builder()
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
