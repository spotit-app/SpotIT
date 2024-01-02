package com.spotit.backend.employee.userAccount;

import static com.spotit.backend.storage.StorageServiceImpl.getFilenameFromUrl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.storage.StorageService;

import jakarta.transaction.Transactional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final StorageService storageService;

    private static final String PROFILE_PICTURES_DIRECTORY_NAME = "profilePictures";

    public UserAccountServiceImpl(
            UserAccountRepository userAccountRepository,
            StorageService storageService) {
        this.userAccountRepository = userAccountRepository;
        this.storageService = storageService;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }

    @Override
    @Cacheable(key = "#auth0Id")
    public UserAccount getByAuth0Id(String auth0Id) {
        return userAccountRepository.findUserAccountByAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException(auth0Id));
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.auth0Id"))
    public UserAccount create(UserAccount userAccount) {
        try {
            return userAccountRepository.save(userAccount);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.auth0Id"))
    public UserAccount create(UserAccount userAccount, byte[] profilePicture) {
        String profilePictureUrl = storageService.uploadFile(
                profilePicture,
                PROFILE_PICTURES_DIRECTORY_NAME,
                userAccount.getAuth0Id());

        return create(userAccount, profilePictureUrl);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.auth0Id"))
    public UserAccount create(UserAccount userAccount, String profilePictureUrl) {
        userAccount.setProfilePictureUrl(profilePictureUrl);

        return create(userAccount);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#auth0Id"))
    public UserAccount update(String auth0Id, UserAccount userAccount, byte[] profilePicture) {
        UserAccount foundUserAccount = getByAuth0Id(auth0Id);

        if (profilePicture != null) {
            String profilePictureUrl = storageService.uploadFile(
                    profilePicture,
                    PROFILE_PICTURES_DIRECTORY_NAME,
                    foundUserAccount.getAuth0Id() + System.currentTimeMillis());

            userAccount.setProfilePictureUrl(profilePictureUrl);

            try {
                storageService.deleteFile(getFilenameFromUrl(foundUserAccount.getProfilePictureUrl()));
            } catch (Exception err) {
            }
        }

        foundUserAccount.update(userAccount);

        return userAccountRepository.save(foundUserAccount);
    }

    @Override
    @Transactional
    @CacheEvict(key = "{#auth0Id, 'all'}")
    public void delete(String auth0Id) {
        UserAccount accountToDelete = getByAuth0Id(auth0Id);

        storageService.deleteFile(accountToDelete.getAuth0Id());

        userAccountRepository.delete(accountToDelete);
    }
}
