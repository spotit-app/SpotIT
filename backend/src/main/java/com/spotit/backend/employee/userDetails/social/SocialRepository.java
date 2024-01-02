package com.spotit.backend.employee.userDetails.social;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface SocialRepository extends AbstractUserDetailRepository<Social, Integer> {
}
