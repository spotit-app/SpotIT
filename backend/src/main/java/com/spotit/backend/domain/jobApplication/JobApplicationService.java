package com.spotit.backend.domain.jobApplication;

import java.util.List;
import java.util.Optional;

import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationService {

    Page<JobApplication> getAllByJobOfferFilteredByStatus(JobOffer jobOffer,
            Optional<ApplicationStatus> applicationStatus, Pageable pageable);

    Page<JobApplication> getAllByPortfolioFilteredByStatus(Portfolio portfolio,
            Optional<ApplicationStatus> applicationStatus, Pageable pageable);

    List<JobApplication> getAllByJobOfferAndPortfolio(JobOffer jobOffer, Portfolio portfolio);

    JobApplication create(JobApplication jobApplicationToCreate);

    JobApplication changeStatus(Integer jobApplicationId, ApplicationStatus applicationStatus);

    JobApplication getById(Integer jobApplicationId);
}
