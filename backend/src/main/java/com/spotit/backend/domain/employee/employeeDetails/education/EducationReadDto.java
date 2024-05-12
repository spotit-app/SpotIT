package com.spotit.backend.domain.employee.employeeDetails.education;

import java.time.LocalDate;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record EducationReadDto(
        Integer id,
        String schoolName,
        String faculty,
        LocalDate startDate,
        LocalDate endDate,
        String educationLevel) implements ReadDto {
}
