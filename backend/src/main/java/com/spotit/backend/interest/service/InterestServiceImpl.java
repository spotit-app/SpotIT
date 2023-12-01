package com.spotit.backend.interest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.interest.model.Interest;
import com.spotit.backend.interest.repository.InterestRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class InterestServiceImpl
        extends AbstractServiceImpl<Interest, Integer>
        implements InterestService {

    private final UserAccountService userAccountService;
    private final InterestRepository interestRepository;

    public InterestServiceImpl(InterestRepository interestRepository, UserAccountService userAccountService) {
        super(interestRepository);
        this.userAccountService = userAccountService;
        this.interestRepository = interestRepository;
    }

    @Override
    public List<Interest> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return interestRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public Interest create(String userAccountAuth0Id, Interest interest) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        interest.setUserAccount(userAccount);

        return create(interest);
    }
}
