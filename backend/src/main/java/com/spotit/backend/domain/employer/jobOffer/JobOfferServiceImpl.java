package com.spotit.backend.domain.employer.jobOffer;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.CachePut;
// import org.springframework.cache.annotation.Cacheable;
// import org.springframework.cache.annotation.Caching;
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
    // @Cacheable(key = "#id")
    public JobOffer getById(Integer id) {
        return jobOfferRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    // @Cacheable(key = "#company.id")
    public Page<JobOffer> getAllByCompany(Company company, Pageable pageable) {
        return jobOfferRepository.findByCompany(company, pageable);
    }

    @Override
    // @Caching(evict = @CacheEvict(key = "#company.id"), put = @CachePut(key =
    // "#result.id"))
    public JobOffer create(JobOffer jobOffer, Company company, WorkExperience workExperience,
            List<TechSkillName> techSkillNames, List<SoftSkillName> softSkillNames,
            List<ForeignLanguageName> foreignLanguageNames, List<WorkMode> workModes) {
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
    // @Caching(evict = @CacheEvict(key = "#result.company.id"), put = @CachePut(key
    // = "#id"))
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
    // @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        JobOffer jobOfferToDelete = getById(id);

        jobOfferRepository.delete(jobOfferToDelete);
    }

    @Override
    // @Cacheable(key = "#bookSearchCriteria?.toString() + #pageable?.toString()")
    public Page<JobOffer> findByCriteria(
            Supplier<Specification<JobOffer>> jobOfferSearchCriteria,
            Pageable pageable) {
        return jobOfferRepository.findAll(jobOfferSearchCriteria.get(), pageable);
    }
}
