package com.spotit.backend.employee.userDetails.course;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

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
