package com.spotit.backend.domain.jobApplication;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employee.portfolio.PortfolioService;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.employer.jobOffer.JobOfferService;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobOfferService jobOfferService;
    private final PortfolioService portfolioService;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplicationController(
            JobApplicationService jobApplicationService,
            JobOfferService jobOfferService,
            PortfolioService portfolioService,
            JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationService = jobApplicationService;
        this.jobOfferService = jobOfferService;
        this.portfolioService = portfolioService;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @GetMapping("/userAccount/{auth0Id}/company/{companyId}/jobOffer/{jobOfferId}/application")
    public Page<JobApplicationReadDto> getJobOfferApplications(
            @PathVariable Integer jobOfferId,
            @PageableDefault(size = 20, sort = "portfolio", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Optional<ApplicationStatus> status) {
        JobOffer jobOffer = jobOfferService.getById(jobOfferId);

        return jobApplicationService
                .getAllByJobOfferFilteredByStatus(jobOffer, status, pageable)
                .map(jobApplicationMapper::toReadDto);
    }

    @GetMapping("/jobOffer/{jobOfferId}/application/{auth0Id}")
    public Map<String, Boolean> checkOfferData(@PathVariable Integer jobOfferId, @PathVariable String auth0Id) {
        Portfolio portfolio = portfolioService.getByUserAccountAuth0Id(auth0Id);
        JobOffer jobOffer = jobOfferService.getById(jobOfferId);

        List<JobApplication> applications = jobApplicationService.getAllByJobOfferAndPortfolio(jobOffer, portfolio);

        Map<String, Boolean> response = new HashMap<>();
        response.put("hasUserApplied", !applications.isEmpty());
        response.put("isUserOffer", jobOffer.getCompany().getUserAccount().getAuth0Id().equals(auth0Id));
        return response;
    }

    @GetMapping("/userAccount/{auth0Id}/application")
    public Page<JobApplicationUserReadDto> getJobApplicationsOfUser(
            @PathVariable String auth0Id,
            @PageableDefault(size = 20, sort = "applicationStatus", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Optional<ApplicationStatus> status) {
        Portfolio portfolio = portfolioService.getByUserAccountAuth0Id(auth0Id);

        return jobApplicationService.getAllByPortfolioFilteredByStatus(portfolio, status, pageable)
                .map(jobApplicationMapper::toUserReadDto);
    }

    @PostMapping("/jobOffer/{jobOfferId}/application")
    public JobApplicationReadDto createJobOfferApplication(
            @PathVariable Integer jobOfferId,
            @RequestBody JobApplicationWriteDto writeDto) {
        JobOffer jobOffer = jobOfferService.getById(jobOfferId);
        Portfolio portfolio = portfolioService.getByUserAccountAuth0Id(writeDto.auth0Id());

        JobApplication jobApplicationToCreate = JobApplication.builder()
                .jobOffer(jobOffer)
                .portfolio(portfolio)
                .applicationStatus(ApplicationStatus.DELIVERED)
                .build();

        return jobApplicationMapper.toReadDto(
                jobApplicationService.create(jobApplicationToCreate));
    }

    @PutMapping("/userAccount/{auth0Id}/company/{companyId}/jobOffer/{jobOfferId}/application/{applicationId}/changeStatus")
    public JobApplicationReadDto changeApplicationStatus(
            @PathVariable Integer applicationId,
            @RequestBody ApplicationStatus newStatus) {
        return jobApplicationMapper.toReadDto(jobApplicationService.changeStatus(applicationId, newStatus));
    }
}
