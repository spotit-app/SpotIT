package com.spotit.backend.domain.employee.employeeDetails.education;

import static com.spotit.backend.domain.referenceData.educationLevel.EducationLevelUtils.createEducationLevel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.referenceData.educationLevel.EducationLevel;

public interface EducationUtils {

    static Education createEducation(Integer id) {
        EducationLevel educationLevel = createEducationLevel(id);
        return Education.builder()
                .id(id)
                .educationLevel(educationLevel)
                .schoolName("SchoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static EducationReadDto createEducationReadDto(Integer id) {
        return EducationReadDto.builder()
                .id(id)
                .educationLevel("EducationLevel " + id)
                .schoolName("schoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static EducationWriteDto createEducationWriteDto(int id) {
        return EducationWriteDto.builder()
                .educationLevel("EducationLevel " + id)
                .schoolName("schoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    static List<Education> generateEducationList(int length) {
        return IntStream.range(0, length)
                .mapToObj(EducationUtils::createEducation)
                .toList();
    }
}
