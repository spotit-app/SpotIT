package com.spotit.backend.employee.userDetails.education;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.ReadDto;

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
