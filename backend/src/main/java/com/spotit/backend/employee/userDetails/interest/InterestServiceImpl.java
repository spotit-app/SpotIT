package com.spotit.backend.employee.userDetails.interest;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class InterestServiceImpl
        extends AbstractUserDetailServiceImpl<Interest, Integer>
        implements InterestService {

    public InterestServiceImpl(
            InterestRepository interestRepository,
            UserAccountService userAccountService) {
        super(interestRepository, userAccountService);
    }
}
