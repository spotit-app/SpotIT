package com.spotit.backend.domain.employee.employeeDetails.experience;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class ExperienceServiceImpl
        extends AbstractUserDetailServiceImpl<Experience, Integer>
        implements ExperienceService {

    public ExperienceServiceImpl(
            ExperienceRepository experienceRepository,
            UserAccountService userAccountService) {
        super(experienceRepository, userAccountService);
    }
}
