package com.spotit.backend.domain.jobApplication;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Integer>, JpaSpecificationExecutor<JobApplication> {

    Page<JobApplication> findByJobOffer(JobOffer jobOffer, Pageable pageable);

    Page<JobApplication> findByPortfolio(Portfolio portfolio, Pageable pageable);

    List<JobApplication> findByJobOfferAndPortfolio(JobOffer jobOffer, Portfolio portfolio);

    Page<JobApplication> findByJobOfferAndApplicationStatus(JobOffer jobOffer, ApplicationStatus applicationStatus,
            Pageable pageable);

    Page<JobApplication> findByPortfolioAndApplicationStatus(Portfolio portfolio, ApplicationStatus applicationStatus,
            Pageable pageable);
}
