package com.spotit.backend.domain.referenceData.workExperience;

import java.util.List;
import java.util.stream.IntStream;

public interface WorkExperienceUtils {

    static WorkExperience createWorkExperience(int id) {
        return WorkExperience.builder()
                .name("WorkExperience " + id)
                .build();
    }

    static WorkExperienceReadDto createWorkExperienceReadDto(int id) {
        return WorkExperienceReadDto.builder()
                .name("WorkExperience " + id)
                .build();
    }

    public static WorkExperienceWriteDto createWorkExperienceWriteDto(int id) {
        return WorkExperienceWriteDto.builder()
                .name("WorkExperience " + id)
                .build();
    }

    public static List<WorkExperience> generateWorkExperienceList(int length) {
        return IntStream.range(0, length)
                .mapToObj(WorkExperienceUtils::createWorkExperience)
                .toList();
    }
}
