package com.spotit.backend.domain.employee.employeeDetails.course;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface CourseRepository extends AbstractUserDetailRepository<Course, Integer> {
}
