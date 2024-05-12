package com.spotit.backend.domain.employee.employeeDetails.social;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class SocialServiceImpl
        extends AbstractUserDetailServiceImpl<Social, Integer>
        implements SocialService {

    public SocialServiceImpl(
            SocialRepository socialRepository,
            UserAccountService userAccountService) {
        super(socialRepository, userAccountService);
    }
}
