package com.spotit.backend.domain.userAccount;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record UserAccountWriteDto(
        String auth0Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String position,
        String description,
        String cvClause,
        Boolean isOpen) implements WriteDto {
}
