package com.spotit.backend.employee.userDetails.social;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record SocialWriteDto(
        String name,
        String socialUrl) implements WriteDto {
}
