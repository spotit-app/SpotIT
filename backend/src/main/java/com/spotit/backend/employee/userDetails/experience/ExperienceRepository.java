package com.spotit.backend.employee.userDetails.experience;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ExperienceRepository extends AbstractUserDetailRepository<Experience, Integer> {
}
