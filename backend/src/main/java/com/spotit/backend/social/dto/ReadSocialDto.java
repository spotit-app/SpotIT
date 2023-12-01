package com.spotit.backend.social.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadSocialDto(
        Integer id,
        String name,
        String socialUrl) implements ReadDto {
}
