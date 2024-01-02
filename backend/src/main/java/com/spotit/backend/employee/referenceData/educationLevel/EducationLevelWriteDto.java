package com.spotit.backend.employee.referenceData.educationLevel;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record EducationLevelWriteDto(
        String name) implements WriteDto {

}
