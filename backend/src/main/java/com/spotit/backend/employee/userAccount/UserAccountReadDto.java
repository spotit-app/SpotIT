package com.spotit.backend.employee.userAccount;

import com.spotit.backend.employee.abstraction.ReadDto;

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
        String cvClause) implements ReadDto {
}
