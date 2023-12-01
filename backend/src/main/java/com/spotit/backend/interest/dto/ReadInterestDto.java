package com.spotit.backend.interest.dto;

import com.spotit.backend.abstraction.dto.ReadDto;

import lombok.Builder;

@Builder
public record ReadInterestDto(
        Integer id,
        String name) implements ReadDto {
}
