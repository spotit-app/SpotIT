package com.spotit.backend.employee.userDetails.social;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

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
