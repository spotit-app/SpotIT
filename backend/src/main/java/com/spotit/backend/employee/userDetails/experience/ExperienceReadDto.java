package com.spotit.backend.employee.userDetails.experience;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record ExperienceReadDto(
        Integer id,
        String companyName,
        String position,
        LocalDate startDate,
        LocalDate endDate) implements ReadDto {
}
