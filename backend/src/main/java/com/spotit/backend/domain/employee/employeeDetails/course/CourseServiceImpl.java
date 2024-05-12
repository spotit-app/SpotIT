package com.spotit.backend.domain.employee.employeeDetails.course;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class CourseServiceImpl
        extends AbstractUserDetailServiceImpl<Course, Integer>
        implements CourseService {

    public CourseServiceImpl(
            CourseRepository courseRepository,
            UserAccountService userAccountService) {
        super(courseRepository, userAccountService);
    }
}
