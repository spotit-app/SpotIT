package com.spotit.backend.domain.employer.jobOffer;

import static java.util.stream.Collectors.toCollection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.employer.company.CompanyService;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameService;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameService;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameService;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperience;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperienceService;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;
import com.spotit.backend.domain.referenceData.workMode.WorkModeService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final CompanyService companyService;
    private final WorkExperienceService workExperienceService;
    private final TechSkillNameService techSkillNameService;
    private final SoftSkillNameService softSkillNameService;
    private final ForeignLanguageNameService foreignLanguageNameService;
    private final WorkModeService workModeService;
    private final JobOfferMapper jobOfferMapper;

    public JobOfferController(
            JobOfferService jobOfferService,
            CompanyService companyService,
            WorkExperienceService workExperienceService,
            WorkModeService workModeService,
            TechSkillNameService techSkillNameService,
            SoftSkillNameService softSkillNameService,
            ForeignLanguageNameService foreignLanguageNameService,
            JobOfferMapper jobOfferMapper) {
        this.jobOfferService = jobOfferService;
        this.companyService = companyService;
        this.workExperienceService = workExperienceService;
        this.techSkillNameService = techSkillNameService;
        this.softSkillNameService = softSkillNameService;
        this.foreignLanguageNameService = foreignLanguageNameService;
        this.workModeService = workModeService;
        this.jobOfferMapper = jobOfferMapper;
    }

    @GetMapping("/jobOffer")
    public Page<JobOfferListReadDto> getJobOffers(
            @PageableDefault(size = 5) Pageable pageable,
            JobOfferSearchCriteria jobOfferSearchCriteria) {
        jobOfferSearchCriteria.setMinDueDate(LocalDate.now());

        return jobOfferService.findByCriteria(jobOfferSearchCriteria, pageable)
                .map(jobOfferMapper::toListReadDto);
    }

    @GetMapping("/jobOffer/{id}")
    public JobOfferReadDto getJobOfferById(@PathVariable Integer id) {
        return jobOfferMapper.toReadDto(jobOfferService.getById(id));
    }

    @GetMapping("/company/{companyId}/jobOffer")
    public Page<JobOfferReadDto> getJobOffersOfCompany(
            @PathVariable Integer companyId,
            @PageableDefault(size = 5, sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Company company = companyService.getById(companyId);

        return jobOfferService.getAllByCompany(company, pageable)
                .map(jobOfferMapper::toReadDto);
    }

    @PostMapping("/userAccount/{auth0Id}/company/{companyId}/jobOffer")
    public JobOfferReadDto createJobOffer(
            @PathVariable Integer companyId,
            @RequestBody JobOfferWriteDto writeDto) {

        Company company = companyService.getById(companyId);
        WorkExperience workExperience = workExperienceService.getById(writeDto.workExperienceId());

        List<TechSkillName> techSkillNames = writeDto.techSkillNames().stream()
                .map(name -> techSkillNameService.getByName(name).get())
                .toList();

        List<SoftSkillName> softSkillNames = writeDto.softSkillNames().stream()
                .map(name -> softSkillNameService.getByName(name).get())
                .toList();

        List<ForeignLanguageName> foreignLanguageNames = writeDto.foreignLanguageNamesIds().stream()
                .map(foreignLanguageNameService::getById)
                .toList();

        List<WorkMode> workModes = writeDto.workModesIds().stream()
                .map(workModeService::getById)
                .toList();

        JobOffer jobOfferToCreate = jobOfferMapper.fromWriteDto(writeDto);

        return jobOfferMapper.toReadDto(jobOfferService.create(
                jobOfferToCreate,
                company,
                workExperience,
                techSkillNames,
                softSkillNames,
                foreignLanguageNames,
                workModes));
    }

    @PutMapping("/userAccount/{auth0Id}/company/{companyId}/jobOffer/{id}")
    public JobOfferReadDto updateJobOfferById(@PathVariable Integer id, @RequestBody JobOfferWriteDto writeDto) {
        WorkExperience workExperience = workExperienceService.getById(writeDto.workExperienceId());

        List<TechSkillName> techSkillNames = writeDto.techSkillNames().stream()
                .map(name -> techSkillNameService.getByName(name).get())
                .collect(toCollection(ArrayList::new));

        List<SoftSkillName> softSkillNames = writeDto.softSkillNames().stream()
                .map(name -> softSkillNameService.getByName(name).get())
                .collect(toCollection(ArrayList::new));

        List<ForeignLanguageName> foreignLanguageNames = writeDto.foreignLanguageNamesIds().stream()
                .map(foreignLanguageNameService::getById)
                .collect(toCollection(ArrayList::new));

        List<WorkMode> workModes = writeDto.workModesIds().stream()
                .map(workModeService::getById)
                .collect(toCollection(ArrayList::new));

        JobOffer jobOfferToUpdate = jobOfferMapper.fromWriteDto(writeDto);
        JobOffer editedJobOffer = jobOfferService.update(
                id,
                jobOfferToUpdate,
                workExperience,
                techSkillNames,
                softSkillNames,
                foreignLanguageNames,
                workModes);

        return jobOfferMapper.toReadDto(editedJobOffer);
    }

    @DeleteMapping("/userAccount/{auth0Id}/company/{companyId}/jobOffer/{id}")
    public Map<String, Integer> deleteJobOfferById(@PathVariable Integer id) {
        jobOfferService.delete(id);

        return Map.of("id", id);
    }
}
