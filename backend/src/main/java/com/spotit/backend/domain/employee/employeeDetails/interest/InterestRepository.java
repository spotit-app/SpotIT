package com.spotit.backend.domain.employee.employeeDetails.interest;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface InterestRepository extends AbstractUserDetailRepository<Interest, Integer> {
}
