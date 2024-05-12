package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class ForeignLanguageServiceImpl
        extends AbstractUserDetailServiceImpl<ForeignLanguage, Integer>
        implements ForeignLanguageService {

    public ForeignLanguageServiceImpl(ForeignLanguageRepository foreignLanguageRepository,
            UserAccountService userAccountService) {
        super(foreignLanguageRepository, userAccountService);
    }
}
