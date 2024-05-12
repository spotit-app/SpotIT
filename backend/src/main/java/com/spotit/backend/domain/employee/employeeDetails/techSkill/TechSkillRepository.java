package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface TechSkillRepository extends AbstractUserDetailRepository<TechSkill, Integer> {
}
