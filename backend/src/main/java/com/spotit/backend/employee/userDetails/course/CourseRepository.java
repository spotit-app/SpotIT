package com.spotit.backend.employee.userDetails.course;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface CourseRepository extends AbstractUserDetailRepository<Course, Integer> {
}
