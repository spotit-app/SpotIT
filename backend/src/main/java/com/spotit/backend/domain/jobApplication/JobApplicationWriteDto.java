package com.spotit.backend.domain.jobApplication;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record JobApplicationWriteDto(
        String auth0Id) implements WriteDto {
}
