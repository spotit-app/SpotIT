package com.spotit.backend.social.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteSocialDto(
        String name,
        String socialUrl) implements WriteDto {
}
