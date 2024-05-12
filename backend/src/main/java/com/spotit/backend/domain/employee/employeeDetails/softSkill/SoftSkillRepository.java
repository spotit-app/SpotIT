package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface SoftSkillRepository extends AbstractUserDetailRepository<SoftSkill, Integer> {
}
