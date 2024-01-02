package com.spotit.backend.employee.userDetails.techSkill;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface TechSkillRepository extends AbstractUserDetailRepository<TechSkill, Integer> {
}
