package com.spotit.backend.employee.userDetails.course;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.ReadDto;

import lombok.Builder;

@Builder
public record CourseReadDto(
        Integer id,
        String name,
        LocalDate finishDate) implements ReadDto {
}
