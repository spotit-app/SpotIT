package com.spotit.backend.employee.userDetails.education;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface EducationRepository extends AbstractUserDetailRepository<Education, Integer> {
}
