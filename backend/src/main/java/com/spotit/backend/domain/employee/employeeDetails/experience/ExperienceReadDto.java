package com.spotit.backend.domain.employee.employeeDetails.experience;

import java.time.LocalDate;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ExperienceReadDto(
        Integer id,
        String companyName,
        String position,
        LocalDate startDate,
        LocalDate endDate) implements ReadDto {
}
