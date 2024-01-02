package com.spotit.backend.employee.userDetails.foreignLanguage;

import org.springframework.stereotype.Repository;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailRepository;

@Repository
public interface ForeignLanguageRepository extends AbstractUserDetailRepository<ForeignLanguage, Integer> {
}
