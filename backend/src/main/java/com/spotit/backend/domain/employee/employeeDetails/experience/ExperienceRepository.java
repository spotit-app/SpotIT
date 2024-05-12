package com.spotit.backend.domain.employee.employeeDetails.experience;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ExperienceRepository extends AbstractUserDetailRepository<Experience, Integer> {
}
