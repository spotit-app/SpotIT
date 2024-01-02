package com.spotit.backend.employee.referenceData.educationLevel;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record EducationLevelReadDto(
        Integer id,
        String name) implements ReadDto {

}
