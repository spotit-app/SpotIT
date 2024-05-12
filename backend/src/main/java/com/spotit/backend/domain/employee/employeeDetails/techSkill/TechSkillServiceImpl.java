package com.spotit.backend.domain.employee.employeeDetails.techSkill;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

@Service
public class TechSkillServiceImpl
        extends AbstractUserDetailServiceImpl<TechSkill, Integer>
        implements TechSkillService {

    public TechSkillServiceImpl(TechSkillRepository techSkillRepository, UserAccountService userAccountService) {
        super(techSkillRepository, userAccountService);
    }
}
