package com.spotit.backend.domain.employer.jobOffer;

import java.time.LocalDate;
import java.util.List;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record JobOfferWriteDto(
        String name,
        String position,
        String description,
        Integer minSalary,
        Integer maxSalary,
        String benefits,
        LocalDate dueDate,
        Integer workExperienceId,
        List<String> techSkillNames,
        List<String> softSkillNames,
        List<Integer> foreignLanguageNamesIds,
        List<Integer> workModesIds) implements WriteDto {
}
