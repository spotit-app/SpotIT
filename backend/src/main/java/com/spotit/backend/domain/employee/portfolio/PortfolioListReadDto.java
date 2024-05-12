package com.spotit.backend.domain.employee.portfolio;

import com.spotit.backend.abstraction.ReadDto;
import com.spotit.backend.domain.userAccount.UserAccountReadDataDto;

import lombok.Builder;

@Builder
public record PortfolioListReadDto(
        String portfolioUrl,
        UserAccountReadDataDto userData) implements ReadDto {
}
