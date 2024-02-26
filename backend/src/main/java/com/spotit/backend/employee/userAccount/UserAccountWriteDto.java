package com.spotit.backend.employee.userAccount;

import com.spotit.backend.employee.abstraction.WriteDto;

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
