package com.spotit.backend.experience.dto;

import java.time.LocalDate;

public record ExperienceDto(
        Integer id,
        String companyName,
        String position,
        LocalDate startDate,
        LocalDate endDate) {
}
