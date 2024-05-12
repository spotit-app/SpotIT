package com.spotit.backend.domain.employee.employeeDetails.interest;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

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
