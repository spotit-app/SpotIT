package com.spotit.backend.employee.portfolio;

import com.spotit.backend.employee.abstraction.ReadDto;
import com.spotit.backend.employee.userAccount.UserAccountReadDataDto;

import lombok.Builder;

@Builder
public record PortfolioListReadDto(
        String portfolioUrl,
        UserAccountReadDataDto userData) implements ReadDto {
}
