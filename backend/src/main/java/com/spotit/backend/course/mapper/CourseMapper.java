package com.spotit.backend.course.mapper;

import org.mapstruct.Mapper;

import com.spotit.backend.course.dto.CourseDto;
import com.spotit.backend.course.model.Course;

@Mapper
public interface CourseMapper {

    CourseDto toDto(Course course);
}
