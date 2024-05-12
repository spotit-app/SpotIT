package com.spotit.backend.domain.employee.employeeDetails.education;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class EducationServiceImpl
        extends AbstractUserDetailServiceImpl<Education, Integer>
        implements EducationService {

    public EducationServiceImpl(EducationRepository educationRepository,
            UserAccountService userAccountService) {
        super(educationRepository, userAccountService);
    }
}
