package com.spotit.backend.domain.employee.employeeDetails.course;

import java.time.LocalDate;

import com.spotit.backend.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record CourseWriteDto(
        String name,
        LocalDate finishDate) implements WriteDto {
}
