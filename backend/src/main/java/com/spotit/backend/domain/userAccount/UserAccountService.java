package com.spotit.backend.domain.userAccount;

public interface UserAccountService {

    UserAccount getByAuth0Id(String auth0Id);

    UserAccount create(UserAccount userAccount);

    UserAccount create(UserAccount userAccount, byte[] profilePicture);

    UserAccount create(UserAccount userAccount, String profilePictureUrl);

    UserAccount update(String auth0Id, UserAccount userAccount, byte[] profilePicture);

    void delete(String auth0Id);
}
