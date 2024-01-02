package com.spotit.backend.employee.userDetails.course;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface CourseMapper extends EntityMapper<Course, CourseReadDto, CourseWriteDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    Course fromWriteDto(CourseWriteDto writeDto);

    @Override
    CourseReadDto toReadDto(Course course);
}
