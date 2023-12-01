package com.spotit.backend.course.dto;

import java.time.LocalDate;

public record CourseDto(
        Integer id,
        String name,
        LocalDate finishDate) {
}
