package com.spotit.backend.employee.userDetails.project;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ProjectRepository extends AbstractUserDetailRepository<Project, Integer> {
}
