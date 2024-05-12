package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import org.springframework.stereotype.Repository;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ForeignLanguageRepository extends AbstractUserDetailRepository<ForeignLanguage, Integer> {
}
