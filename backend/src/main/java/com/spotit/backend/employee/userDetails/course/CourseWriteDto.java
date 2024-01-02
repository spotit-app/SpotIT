package com.spotit.backend.employee.userDetails.course;

import java.time.LocalDate;

import com.spotit.backend.employee.abstraction.WriteDto;

import lombok.Builder;

@Builder
public record CourseWriteDto(
        String name,
        LocalDate finishDate) implements WriteDto {
}
