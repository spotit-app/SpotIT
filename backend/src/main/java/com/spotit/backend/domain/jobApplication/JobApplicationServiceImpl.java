package com.spotit.backend.domain.jobApplication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    protected final JobApplicationRepository jobApplicationRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public Page<JobApplication> getAllByJobOfferFilteredByStatus(
            JobOffer jobOffer,
            Optional<ApplicationStatus> applicationStatus,
            Pageable pageable) {
        if (applicationStatus.isPresent()) {
            return jobApplicationRepository.findByJobOfferAndApplicationStatus(jobOffer, applicationStatus.get(),
                    pageable);
        } else {
            return jobApplicationRepository.findByJobOffer(jobOffer, pageable);
        }
    }

    @Override
    public Page<JobApplication> getAllByPortfolioFilteredByStatus(
            Portfolio portfolio,
            Optional<ApplicationStatus> applicationStatus,
            Pageable pageable) {
        if (applicationStatus.isPresent()) {
            return jobApplicationRepository.findByPortfolioAndApplicationStatus(portfolio, applicationStatus.get(),
                    pageable);
        } else {
            return jobApplicationRepository.findByPortfolio(portfolio, pageable);
        }
    }

    @Override
    public List<JobApplication> getAllByJobOfferAndPortfolio(JobOffer jobOffer, Portfolio portfolio) {
        return jobApplicationRepository.findByJobOfferAndPortfolio(jobOffer, portfolio);
    }

    @Override
    public JobApplication create(JobApplication jobApplication) {
        try {
            return jobApplicationRepository.save(jobApplication);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    public JobApplication changeStatus(Integer jobApplicationId, ApplicationStatus applicationStatus) {
        JobApplication jobApplication = getById(jobApplicationId);

        jobApplication.setApplicationStatus(applicationStatus);

        return create(jobApplication);
    }

    @Override
    public JobApplication getById(Integer jobApplicationId) {
        return jobApplicationRepository
                .findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException(jobApplicationId));
    }
}
