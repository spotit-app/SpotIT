package com.spotit.backend.domain.employee.employeeDetails.project;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ProjectRepository extends AbstractUserDetailRepository<Project, Integer> {
}
