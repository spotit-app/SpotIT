package com.spotit.backend.domain.jobApplication;

import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOffer;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOfferReadDto;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employee.portfolio.PortfolioReadDto;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.employer.jobOffer.JobOfferReadDto;

public interface JobApplicationUtils {

    static JobApplication createJobApplication(Integer id) {
        JobOffer jobOffer = createJobOffer(id);
        Portfolio portfolio = Portfolio.builder().portfolioUrl("portfolio-url" + id).build();

        return JobApplication.builder()
                .id(id)
                .jobOffer(jobOffer)
                .portfolio(portfolio)
                .build();
    }

    static JobApplicationReadDto createJobApplicationReadDto(Integer id) {
        JobOfferReadDto jobOffer = createJobOfferReadDto(id);
        PortfolioReadDto portfolio = PortfolioReadDto.builder().portfolioUrl("portfolio-url" + id).build();

        return JobApplicationReadDto.builder()
                .id(id)
                .jobOffer(jobOffer)
                .portfolio(portfolio)
                .build();
    }

    static JobApplicationUserReadDto createJobApplicationUserReadDto(Integer id) {
        JobOfferReadDto jobOffer = createJobOfferReadDto(id);

        return JobApplicationUserReadDto.builder()
                .id(id)
                .jobOffer(jobOffer)
                .build();
    }

    static JobApplicationWriteDto createJobApplicationWriteDto(Integer id) {

        return JobApplicationWriteDto.builder()
                .auth0Id("auth0Id" + id)
                .build();
    }

    static List<JobApplication> generateJobApplicationsList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(JobApplicationUtils::createJobApplication)
                .toList();
    }
}
