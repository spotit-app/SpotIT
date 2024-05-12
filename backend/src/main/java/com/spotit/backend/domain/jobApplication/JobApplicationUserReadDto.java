package com.spotit.backend.domain.jobApplication;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.employer.jobOffer.JobOfferReadDto;

import lombok.Builder;

@Builder
public record JobApplicationUserReadDto(
        Integer id,
        JobOfferReadDto jobOffer,
        String applicationStatus) implements ReadDto {
}
