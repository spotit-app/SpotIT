package com.spotit.backend.employee.userDetails.softSkill;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface SoftSkillRepository extends AbstractUserDetailRepository<SoftSkill, Integer> {
}
