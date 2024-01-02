package com.spotit.backend.employee.userDetails.techSkill;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class TechSkillServiceImpl
        extends AbstractUserDetailServiceImpl<TechSkill, Integer>
        implements TechSkillService {

    public TechSkillServiceImpl(TechSkillRepository techSkillRepository, UserAccountService userAccountService) {
        super(techSkillRepository, userAccountService);
    }
}
