package com.spotit.backend.userAccount.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteUserAccountDto(
        String auth0Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String position,
        String description,
        String cvClause) implements WriteDto {
}
