package com.spotit.backend.domain.jobApplication;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employee.portfolio.PortfolioReadDto;
import com.spotit.backend.domain.employer.jobOffer.JobOfferReadDto;

import lombok.Builder;

@Builder
public record JobApplicationReadDto(
        Integer id,
        JobOfferReadDto jobOffer,
        PortfolioReadDto portfolio,
        String applicationStatus) implements ReadDto {
}
