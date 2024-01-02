package com.spotit.backend.employee.userDetails.education;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record EducationWriteDto(
        String schoolName,
        String faculty,
        LocalDate startDate,
        LocalDate endDate,
        String educationLevel) implements WriteDto {
}
