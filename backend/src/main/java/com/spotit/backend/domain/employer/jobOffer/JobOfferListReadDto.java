package com.spotit.backend.domain.employer.jobOffer;

import java.time.LocalDate;
import java.util.List;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employer.company.CompanyReadDto;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameReadDto;

import lombok.Builder;

@Builder
public record JobOfferListReadDto(
        Integer id,
        String name,
        String position,
        Integer minSalary,
        Integer maxSalary,
        LocalDate dueDate,
        CompanyReadDto company,
        String workExperienceName,
        List<TechSkillNameReadDto> techSkillNames) implements ReadDto {
}
