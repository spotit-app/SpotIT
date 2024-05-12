package com.spotit.backend.domain.employee.employeeDetails.experience;

import java.time.LocalDate;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ExperienceWriteDto(
        String companyName,
        String position,
        LocalDate startDate,
        LocalDate endDate) implements WriteDto {
}
