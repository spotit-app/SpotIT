package com.spotit.backend.employee.userAccount;

import java.util.List;

public interface UserAccountService {

    List<UserAccount> getAll();

    UserAccount getByAuth0Id(String auth0Id);

    UserAccount create(UserAccount userAccount);

    UserAccount create(UserAccount userAccount, byte[] profilePicture);

    UserAccount create(UserAccount userAccount, String profilePictureUrl);

    UserAccount update(String auth0Id, UserAccount userAccount, byte[] profilePicture);

    void delete(String auth0Id);
}
