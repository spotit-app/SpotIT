package com.spotit.backend.softSkill.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.softSkill.model.SoftSkill;
import com.spotit.backend.softSkill.repository.SoftSkillRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class SoftSkillServiceImpl
        extends AbstractServiceImpl<SoftSkill, Integer>
        implements SoftSkillService {

    private final SoftSkillRepository softSkillRepository;
    private final UserAccountService userAccountService;

    public SoftSkillServiceImpl(SoftSkillRepository softSkillRepository, UserAccountService userAccountService) {
        super(softSkillRepository);
        this.softSkillRepository = softSkillRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public List<SoftSkill> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return softSkillRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public SoftSkill create(String userAccountAuth0Id, SoftSkill softSkill) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        softSkill.setUserAccount(userAccount);

        return create(softSkill);
    }
}
