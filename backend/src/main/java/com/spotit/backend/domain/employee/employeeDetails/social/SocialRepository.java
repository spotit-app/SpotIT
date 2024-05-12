package com.spotit.backend.domain.employee.employeeDetails.social;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface SocialRepository extends AbstractUserDetailRepository<Social, Integer> {
}
