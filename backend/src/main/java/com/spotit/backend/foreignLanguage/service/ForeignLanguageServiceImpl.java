package com.spotit.backend.foreignLanguage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;
import com.spotit.backend.foreignLanguage.repository.ForeignLanguageRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class ForeignLanguageServiceImpl
        extends AbstractServiceImpl<ForeignLanguage, Integer>
        implements ForeignLanguageService {

    private final ForeignLanguageRepository foreignLanguageRepository;
    private final UserAccountService userAccountService;

    public ForeignLanguageServiceImpl(ForeignLanguageRepository foreignLanguageRepository,
            UserAccountService userAccountService) {
        super(foreignLanguageRepository);
        this.foreignLanguageRepository = foreignLanguageRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public List<ForeignLanguage> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return foreignLanguageRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public ForeignLanguage create(String userAccountAuth0Id, ForeignLanguage foreignLanguage) {
            UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
            foreignLanguage.setUserAccount(userAccount);

            return create(foreignLanguage);
    }
}
