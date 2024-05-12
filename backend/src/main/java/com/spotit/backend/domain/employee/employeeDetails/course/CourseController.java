package com.spotit.backend.domain.employee.employeeDetails.course;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/course")
public class CourseController
        extends AbstractUserDetailController<Course, Integer, CourseReadDto, CourseWriteDto> {

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        super(courseService, courseMapper);
    }
}
