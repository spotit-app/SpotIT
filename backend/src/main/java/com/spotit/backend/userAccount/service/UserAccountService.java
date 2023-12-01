package com.spotit.backend.userAccount.service;

import java.util.List;

import com.spotit.backend.abstraction.service.AbstractService;
import com.spotit.backend.userAccount.model.UserAccount;

public interface UserAccountService extends AbstractService<UserAccount, Integer> {

    List<UserAccount> getAll();

    UserAccount getByAuth0Id(String auth0Id);

    void delete(String auth0Id);

    UserAccount update(String auth0Id, UserAccount userAccount, byte[] profilePicture);

    UserAccount create(UserAccount userAccount, byte[] profilePicture);
}
