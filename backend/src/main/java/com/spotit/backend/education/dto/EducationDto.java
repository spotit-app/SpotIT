package com.spotit.backend.education.dto;

import java.time.LocalDate;

public record EducationDto(
        Integer id,
        String schoolName,
        String faculty,
        LocalDate startDate,
        LocalDate endDate,
        String educationLevelName) {
}
