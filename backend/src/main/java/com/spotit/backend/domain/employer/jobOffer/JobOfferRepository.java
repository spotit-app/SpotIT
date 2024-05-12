package com.spotit.backend.domain.employer.jobOffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.spotit.backend.domain.employer.company.Company;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Integer>, JpaSpecificationExecutor<JobOffer> {

    Page<JobOffer> findByCompany(Company company, Pageable pageable);
}
