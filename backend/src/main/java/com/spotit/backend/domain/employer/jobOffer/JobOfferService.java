package com.spotit.backend.domain.employer.jobOffer;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperience;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;

public interface JobOfferService {

    JobOffer getById(Integer id);

    Page<JobOffer> getAllByCompany(Company company, Pageable pageable);

    JobOffer create(
            JobOffer jobOfferToCreate,
            Company company,
            WorkExperience workExperience,
            List<TechSkillName> techSkillNames,
            List<SoftSkillName> softSkillNames,
            List<ForeignLanguageName> foreignLanguageNames,
            List<WorkMode> workModes);

    JobOffer update(
            Integer id,
            JobOffer jobOfferToUpdate,
            WorkExperience workExperience,
            List<TechSkillName> techSkillNames,
            List<SoftSkillName> softSkillNames,
            List<ForeignLanguageName> foreignLanguageNames,
            List<WorkMode> workModes);

    void delete(Integer id);

    Page<JobOffer> findByCriteria(Supplier<Specification<JobOffer>> jobOfferSearchCriteria, Pageable pageable);
}
