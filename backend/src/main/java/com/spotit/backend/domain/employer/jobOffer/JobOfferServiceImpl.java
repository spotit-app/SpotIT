package com.spotit.backend.domain.employer.jobOffer;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperience;
import com.spotit.backend.domain.referenceData.workMode.WorkMode;

import jakarta.transaction.Transactional;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    protected final JobOfferRepository jobOfferRepository;

    public JobOfferServiceImpl(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public JobOffer getById(Integer id) {
        return jobOfferRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public Page<JobOffer> getAllByCompany(Company company, Pageable pageable) {
        return jobOfferRepository.findByCompany(company, pageable);
    }

    @Override
    public JobOffer create(
            JobOffer jobOffer,
            Company company,
            WorkExperience workExperience,
            List<TechSkillName> techSkillNames,
            List<SoftSkillName> softSkillNames,
            List<ForeignLanguageName> foreignLanguageNames,
            List<WorkMode> workModes) {
        jobOffer.setCompany(company);
        jobOffer.setWorkExperience(workExperience);
        jobOffer.setTechSkillNames(techSkillNames);
        jobOffer.setSoftSkillNames(softSkillNames);
        jobOffer.setForeignLanguageNames(foreignLanguageNames);
        jobOffer.setWorkModes(workModes);

        try {
            return jobOfferRepository.save(jobOffer);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    public JobOffer update(
            Integer id,
            JobOffer jobOfferToUpdate,
            WorkExperience workExperience,
            List<TechSkillName> techSkillNames,
            List<SoftSkillName> softSkillNames,
            List<ForeignLanguageName> foreignLanguageNames,
            List<WorkMode> workModes) {
        JobOffer jobOffer = getById(id);

        jobOffer.update(jobOfferToUpdate);

        jobOffer.setWorkExperience(workExperience);
        jobOffer.setTechSkillNames(techSkillNames);
        jobOffer.setSoftSkillNames(softSkillNames);
        jobOffer.setForeignLanguageNames(foreignLanguageNames);
        jobOffer.setWorkModes(workModes);

        return jobOfferRepository.save(jobOffer);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        JobOffer jobOfferToDelete = getById(id);

        jobOfferRepository.delete(jobOfferToDelete);
    }

    @Override
    public Page<JobOffer> findByCriteria(
            Supplier<Specification<JobOffer>> jobOfferSearchCriteria,
            Pageable pageable) {
        return jobOfferRepository.findAll(jobOfferSearchCriteria.get(), pageable);
    }
}
