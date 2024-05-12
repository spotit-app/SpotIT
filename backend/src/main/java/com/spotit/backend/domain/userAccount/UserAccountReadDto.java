package com.spotit.backend.domain.userAccount;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record UserAccountReadDto(
        Integer id,
        String auth0Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String profilePictureUrl,
        String position,
        String description,
        String cvClause,
        Boolean isOpen) implements ReadDto {
}
