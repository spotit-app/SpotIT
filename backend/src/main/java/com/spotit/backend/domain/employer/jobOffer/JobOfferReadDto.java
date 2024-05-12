package com.spotit.backend.domain.employer.jobOffer;

import java.time.LocalDate;
import java.util.List;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employer.company.CompanyReadDto;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameReadDto;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameReadDto;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameReadDto;
import com.spotit.backend.domain.referenceData.workMode.WorkModeReadDto;

import lombok.Builder;

@Builder
public record JobOfferReadDto(
        Integer id,
        String name,
        String position,
        String description,
        Integer minSalary,
        Integer maxSalary,
        String benefits,
        LocalDate dueDate,
        CompanyReadDto company,
        String workExperienceName,
        List<TechSkillNameReadDto> techSkillNames,
        List<SoftSkillNameReadDto> softSkillNames,
        List<ForeignLanguageNameReadDto> foreignLanguageNames,
        List<WorkModeReadDto> workModes) implements ReadDto {
}
