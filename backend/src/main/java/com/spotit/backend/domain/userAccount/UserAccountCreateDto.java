package com.spotit.backend.domain.userAccount;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record UserAccountCreateDto(
        String auth0Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String position,
        String description,
        String cvClause,
        String profilePictureUrl,
        Boolean isOpen) implements WriteDto {
}
