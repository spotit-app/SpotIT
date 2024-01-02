package com.spotit.backend.employee.userDetails.course;

import java.util.List;
import java.util.stream.IntStream;
import java.time.LocalDate;

public interface CourseUtils {

    static Course createCourse(Integer id) {
        return Course.builder()
                .name("Name " + id)
                .finishDate(LocalDate.parse("2023-12-12"))
                .build();
    }

    static CourseReadDto createCourseReadDto(Integer id) {
        return CourseReadDto.builder()
                .name("Name " + id)
                .finishDate(LocalDate.parse("2023-12-12"))
                .build();
    }

    static CourseWriteDto createCourseWriteDto(Integer id) {
        return CourseWriteDto.builder()
                .name("Name " + id)
                .finishDate(LocalDate.parse("2023-12-12"))
                .build();
    }

    static List<Course> generateCourseList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(CourseUtils::createCourse)
                .toList();
    }
}
