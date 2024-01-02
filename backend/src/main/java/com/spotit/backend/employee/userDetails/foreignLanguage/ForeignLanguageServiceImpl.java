package com.spotit.backend.employee.userDetails.foreignLanguage;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class ForeignLanguageServiceImpl
        extends AbstractUserDetailServiceImpl<ForeignLanguage, Integer>
        implements ForeignLanguageService {

    public ForeignLanguageServiceImpl(ForeignLanguageRepository foreignLanguageRepository,
            UserAccountService userAccountService) {
        super(foreignLanguageRepository, userAccountService);
    }
}
