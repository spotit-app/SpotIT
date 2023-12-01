package com.spotit.backend.social.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.social.model.Social;
import com.spotit.backend.social.repository.SocialRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class SocialServiceImpl extends AbstractServiceImpl<Social, Integer> implements SocialService {

    private final SocialRepository socialRepository;
    private final UserAccountService userAccountService;

    public SocialServiceImpl(SocialRepository socialRepository, UserAccountService userAccountService) {
        super(socialRepository);
        this.socialRepository = socialRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public List<Social> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return socialRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public Social create(String userAccountAuth0Id, Social social) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        social.setUserAccount(userAccount);

        return create(social);
    }
}
