package com.spotit.backend.domain.employee.employeeDetails.education;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface EducationRepository extends AbstractUserDetailRepository<Education, Integer> {
}
