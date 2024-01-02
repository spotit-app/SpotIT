package com.spotit.backend.employee.userDetails.education;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class EducationServiceImpl
        extends AbstractUserDetailServiceImpl<Education, Integer>
        implements EducationService {

    public EducationServiceImpl(EducationRepository educationRepository,
            UserAccountService userAccountService) {
        super(educationRepository, userAccountService);
    }
}
