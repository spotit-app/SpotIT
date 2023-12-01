package com.spotit.backend.techSkill.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.techSkill.model.TechSkill;
import com.spotit.backend.techSkill.repository.TechSkillRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class TechSkillServiceImpl
        extends AbstractServiceImpl<TechSkill, Integer>
        implements TechSkillService {

    private final TechSkillRepository techSkillRepository;
    private final UserAccountService userAccountService;

    public TechSkillServiceImpl(TechSkillRepository techSkillRepository, UserAccountService userAccountService) {
        super(techSkillRepository);
        this.techSkillRepository = techSkillRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public List<TechSkill> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return techSkillRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public TechSkill create(String userAccountAuth0Id, TechSkill techSkill) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        techSkill.setUserAccount(userAccount);

        return create(techSkill);
    }
}
