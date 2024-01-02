package com.spotit.backend.employee.referenceData.educationLevel;

import java.util.List;
import java.util.stream.IntStream;

public class EducationLevelUtils {

    public static EducationLevel createEducationLevel(int id) {
        return EducationLevel.builder()
                .name("EducationLevel " + id)
                .custom(false)
                .build();
    }

    public static EducationLevelReadDto createEducationLevelReadDto(int id) {
        return EducationLevelReadDto.builder()
                .name("EducationLevel " + id)
                .build();
    }

    public static EducationLevelWriteDto createEducationLevelWriteDto(int id) {
        return EducationLevelWriteDto.builder()
                .name("EducationLevel " + id)
                .build();
    }

    public static List<EducationLevel> generateEducationLevelList(int length) {
        return IntStream.range(0, length)
                .mapToObj(EducationLevelUtils::createEducationLevel)
                .toList();
    }
}
