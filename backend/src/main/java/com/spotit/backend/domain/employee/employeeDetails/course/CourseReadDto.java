package com.spotit.backend.domain.employee.employeeDetails.course;

import java.time.LocalDate;

import com.spotit.backend.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record CourseReadDto(
        Integer id,
        String name,
        LocalDate finishDate) implements ReadDto {
}
