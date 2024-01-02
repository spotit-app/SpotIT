package com.spotit.backend.employee.userDetails.interest;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface InterestRepository extends AbstractUserDetailRepository<Interest, Integer> {
}
