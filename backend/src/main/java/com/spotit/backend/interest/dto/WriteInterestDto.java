package com.spotit.backend.interest.dto;

import com.spotit.backend.abstraction.dto.WriteDto;

import lombok.Builder;

@Builder
public record WriteInterestDto(String name) implements WriteDto {
}
