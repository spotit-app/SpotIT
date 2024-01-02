package com.spotit.backend.employee.userAccount;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record UserAccountReadDataDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String profilePictureUrl,
        String position,
        String description,
        String cvClause) implements ReadDto {
}
