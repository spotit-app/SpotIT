package com.spotit.backend.employee.userDetails.softSkill;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

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
