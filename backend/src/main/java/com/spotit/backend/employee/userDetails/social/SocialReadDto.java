package com.spotit.backend.employee.userDetails.social;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record SocialReadDto(
        Integer id,
        String name,
        String socialUrl) implements ReadDto {
}
