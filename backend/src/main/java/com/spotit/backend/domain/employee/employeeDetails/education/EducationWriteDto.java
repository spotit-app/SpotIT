package com.spotit.backend.domain.employee.employeeDetails.education;

import java.time.LocalDate;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record EducationWriteDto(
        String schoolName,
        String faculty,
        LocalDate startDate,
        LocalDate endDate,
        String educationLevel) implements WriteDto {
}
