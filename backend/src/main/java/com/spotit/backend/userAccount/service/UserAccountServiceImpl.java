package com.spotit.backend.userAccount.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.storage.service.StorageService;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@Service
public class UserAccountServiceImpl
        extends AbstractServiceImpl<UserAccount, Integer>
        implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final StorageService storageService;

    private static final String PROFILE_PICTURES_DIRECTORY_NAME = "profilePictures";

    public UserAccountServiceImpl(
            UserAccountRepository userAccountRepository,
            StorageService storageService) {
        super(userAccountRepository);
        this.userAccountRepository = userAccountRepository;
        this.storageService = storageService;
    }

    @Override
    public List<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserAccount getByAuth0Id(String auth0Id) {
        return userAccountRepository.findUserAccountByAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException(auth0Id));
    }

    @Override
    @Transactional
    public void delete(String auth0Id) {
        UserAccount accountToDelete = getByAuth0Id(auth0Id);

        storageService.deleteFile(accountToDelete.getAuth0Id());

        userAccountRepository.delete(accountToDelete);
    }

    @Override
    public UserAccount update(String auth0Id, UserAccount userAccount, byte[] profilePicture) {
        UserAccount foundUserAccount = getByAuth0Id(auth0Id);

        if (profilePicture != null) {
            storageService.uploadFile(
                    profilePicture,
                    PROFILE_PICTURES_DIRECTORY_NAME,
                    foundUserAccount.getAuth0Id());
        }

        foundUserAccount.update(userAccount);

        return userAccountRepository.save(foundUserAccount);
    }

    @Override
    public UserAccount create(UserAccount userAccount, byte[] profilePicture) {
        String profilePictureUrl = storageService.uploadFile(
                profilePicture,
                PROFILE_PICTURES_DIRECTORY_NAME,
                userAccount.getAuth0Id());

        userAccount.setProfilePictureUrl(profilePictureUrl);

        return create(userAccount);
    }
}
