package com.spotit.backend.domain.employer.jobOffer;

import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompanyReadDto;
import static com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameUtils.generateForeignLanguageNameList;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.generateSoftSkillNameList;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.createTechSkillNameReadDto;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.generateTechSkillNameList;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperience;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.generateWorkModeList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.employer.company.CompanyReadDto;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameReadDto;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperience;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;

public interface JobOfferUtils {

    static JobOffer createJobOffer(Integer id) {
        Company company = createCompany(id);
        WorkExperience workExperience = createWorkExperience(id);
        List<ForeignLanguageName> foreignLanguageNames = generateForeignLanguageNameList(id);
        List<WorkMode> workModes = generateWorkModeList(id);
        List<TechSkillName> techSkillNames = generateTechSkillNameList(id);
        List<SoftSkillName> softSkillNames = generateSoftSkillNameList(id);

        return JobOffer.builder()
                .name("Name " + id)
                .position("Position " + id)
                .description("Description " + id)
                .minSalary(id)
                .maxSalary(id + 1)
                .benefits("Benefits " + id)
                .dueDate(LocalDate.of(2024, 12, 12))
                .company(company)
                .workExperience(workExperience)
                .foreignLanguageNames(foreignLanguageNames)
                .softSkillNames(softSkillNames)
                .techSkillNames(techSkillNames)
                .workModes(workModes)
                .build();
    }

    static JobOfferListReadDto createJobOfferListReadDto(Integer id) {
        CompanyReadDto companyReadDto = createCompanyReadDto(id);
        List<TechSkillNameReadDto> techSkillNames = List.of(createTechSkillNameReadDto(id));

        return JobOfferListReadDto.builder()
                .id(id)
                .name("Name " + id)
                .minSalary(id)
                .maxSalary(id + 1)
                .dueDate(LocalDate.of(2024, 12, 12))
                .position("Position " + id)
                .company(companyReadDto)
                .workExperienceName("WorkExperience" + id)
                .techSkillNames(techSkillNames)
                .build();
    }

    static JobOfferReadDto createJobOfferReadDto(Integer id) {
        CompanyReadDto companyReadDto = createCompanyReadDto(id);

        return JobOfferReadDto.builder()
                .id(id)
                .name("Name " + id)
                .benefits("Benefits " + id)
                .description("Description " + id)
                .minSalary(id)
                .maxSalary(id + 1)
                .dueDate(LocalDate.of(2024, 12, 12))
                .position("Position " + id)
                .company(companyReadDto)
                .build();
    }

    static JobOfferWriteDto createJobOfferWriteDto(Integer id) {
        WorkExperience workExperience = createWorkExperience(id);
        List<ForeignLanguageName> foreignLanguageNames = generateForeignLanguageNameList(id);
        List<WorkMode> workModes = generateWorkModeList(id);
        List<TechSkillName> techSkillNames = generateTechSkillNameList(id);
        List<SoftSkillName> softSkillNames = generateSoftSkillNameList(id);

        return JobOfferWriteDto.builder()
                .name("Name " + id)
                .benefits("Benefits " + id)
                .description("Description " + id)
                .minSalary(id)
                .maxSalary(id + 1)
                .dueDate(LocalDate.of(2024, 12, 12))
                .position("Position " + id)
                .workExperienceId(workExperience.getId())
                .foreignLanguageNamesIds(foreignLanguageNames.stream().map(ForeignLanguageName::getId).toList())
                .workModesIds(workModes.stream().map(WorkMode::getId).toList())
                .techSkillNames(techSkillNames.stream().map(TechSkillName::getName).toList())
                .softSkillNames(softSkillNames.stream().map(SoftSkillName::getName).toList())
                .build();
    }

    static List<JobOffer> generateJobOffersList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(JobOfferUtils::createJobOffer)
                .toList();
    }
}
