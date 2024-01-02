package com.spotit.backend.employee.userDetails.education;

import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevel;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.IntStream;

import com.spotit.backend.employee.referenceData.educationLevel.EducationLevel;

public class EducationUtils {

    public static Education createEducation(Integer id) {
        EducationLevel educationLevel = createEducationLevel(id);
        Education education = Education.builder()
                .id(id)
                .educationLevel(educationLevel)
                .schoolName("SchoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
        return education;
    }

    public static EducationReadDto createEducationReadDto(Integer id) {
        return EducationReadDto.builder()
                .id(id)
                .educationLevel("EducationLevel " + id)
                .schoolName("schoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    public static EducationWriteDto createEducationWriteDto(int id) {
        return EducationWriteDto.builder()
                .educationLevel("EducationLevel " + id)
                .schoolName("schoolName " + id)
                .faculty("faculty " + id)
                .startDate(LocalDate.parse("2023-12-12"))
                .endDate(LocalDate.parse("2023-12-13"))
                .build();
    }

    public static List<Education> generateEducationList(int length) {
        return IntStream.range(0, length)
                .mapToObj(EducationUtils::createEducation)
                .toList();
    }
}
