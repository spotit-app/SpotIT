package com.spotit.backend.employee.userDetails.experience;

import java.util.List;
import java.util.stream.IntStream;
import java.time.LocalDate;

public interface ExperienceUtils {

    static Experience createExperience(Integer id) {
        return Experience.builder()
                .companyName("CompanyName " + id)
                .position("position " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static ExperienceReadDto createExperienceReadDto(Integer id) {
        return ExperienceReadDto.builder()
                .companyName("CompanyName " + id)
                .position("position " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static ExperienceWriteDto createExperienceWriteDto(Integer id) {
        return ExperienceWriteDto.builder()
                .companyName("CompanyName " + id)
                .position("position " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static List<Experience> generateExperienceList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(ExperienceUtils::createExperience)
                .toList();
    }
}
