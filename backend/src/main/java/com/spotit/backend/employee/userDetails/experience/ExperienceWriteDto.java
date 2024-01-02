package com.spotit.backend.employee.userDetails.experience;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record ExperienceWriteDto(
        String companyName,
        String position,
        LocalDate startDate,
        LocalDate endDate) implements WriteDto {
}
