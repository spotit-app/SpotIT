package com.spotit.backend.employee.userDetails.experience;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class ExperienceServiceImpl
        extends AbstractUserDetailServiceImpl<Experience, Integer>
        implements ExperienceService {

    public ExperienceServiceImpl(ExperienceRepository experienceRepository,
            UserAccountService userAccountService) {
        super(experienceRepository, userAccountService);
    }
}
