package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class SoftSkillServiceImpl
        extends AbstractUserDetailServiceImpl<SoftSkill, Integer>
        implements SoftSkillService {

    public SoftSkillServiceImpl(
            SoftSkillRepository softSkillRepository,
            UserAccountService userAccountService) {
        super(softSkillRepository, userAccountService);
    }
}
