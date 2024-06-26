package com.spotit.backend.domain.userAccount;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.storage.StorageService;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    UserAccountRepository userAccountRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    UserAccountServiceImpl userAccountServiceImpl;

    @Test
    void shouldReturnUserByAuth0Id() {
        // given
        var foundUser = createUserAccount(1);
        var foundUserAuth0Id = foundUser.getAuth0Id();

        when(userAccountRepository.findUserAccountByAuth0Id(foundUserAuth0Id))
                .thenReturn(Optional.of(foundUser));

        // when
        var result = userAccountServiceImpl.getByAuth0Id(foundUserAuth0Id);

        // then
        verify(userAccountRepository, times(1)).findUserAccountByAuth0Id(foundUserAuth0Id);

        assertEquals(foundUser, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        var foundUserAuth0Id = "auth0Id1";

        when(userAccountRepository.findUserAccountByAuth0Id(foundUserAuth0Id))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> userAccountServiceImpl.getByAuth0Id(foundUserAuth0Id));

        // then
        verify(userAccountRepository, times(1)).findUserAccountByAuth0Id(foundUserAuth0Id);

        assertEquals(getEntityNotFoundMessage(foundUserAuth0Id), exception.getMessage());
    }

    @Test
    void shouldReturnUserWhenCreatedSuccessfully() {
        // given
        var userToCreate = createUserAccount(1);
        var userProfilePicture = new byte[0];

        when(storageService.uploadFile(any(), any(), any()))
                .thenReturn("http://profilePicture.url");
        when(userAccountRepository.save(userToCreate))
                .thenReturn(userToCreate);

        // when
        var result = userAccountServiceImpl.create(userToCreate, userProfilePicture);

        // then
        verify(storageService, times(1)).uploadFile(any(), any(), any());
        verify(userAccountRepository, times(1)).save(userToCreate);

        assertEquals(userToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingUser() {
        // given
        var userToCreate = createUserAccount(1);
        var userProfilePicture = new byte[0];

        when(storageService.uploadFile(any(), any(), any()))
                .thenReturn("http://profilePicture.url");
        when(userAccountRepository.save(userToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> userAccountServiceImpl.create(userToCreate, userProfilePicture));

        // then
        verify(storageService, times(1)).uploadFile(any(), any(), any());
        verify(userAccountRepository, times(1)).save(userToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteUser() {
        // given
        var userToDelete = createUserAccount(1);
        var userToDeleteAuth0Id = userToDelete.getAuth0Id();

        when(userAccountRepository.findUserAccountByAuth0Id(userToDeleteAuth0Id))
                .thenReturn(Optional.of(userToDelete));

        // when
        userAccountServiceImpl.delete(userToDeleteAuth0Id);

        // then
        verify(userAccountRepository, times(1)).findUserAccountByAuth0Id(userToDeleteAuth0Id);
        verify(storageService, times(1)).deleteFile(userToDeleteAuth0Id);
        verify(userAccountRepository, times(1)).delete(userToDelete);
    }

    @Test
    void shouldReturnModifiedUser() {
        // given
        var currentUser = createUserAccount(1);
        var modifiedUserAuth0Id = currentUser.getAuth0Id();
        var modifiedUser = createUserAccount(2);
        var userProfilePicture = new byte[0];
        modifiedUser.setAuth0Id(modifiedUserAuth0Id);

        when(storageService.uploadFile(
                any(),
                any(),
                any()))
                .thenReturn("http://profilePicture.url");
        when(userAccountRepository.findUserAccountByAuth0Id(modifiedUserAuth0Id))
                .thenReturn(Optional.of(currentUser));
        when(userAccountRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = userAccountServiceImpl.update(
                modifiedUserAuth0Id,
                modifiedUser,
                userProfilePicture);

        // then
        verify(userAccountRepository, times(1)).findUserAccountByAuth0Id(modifiedUserAuth0Id);
        verify(storageService, times(1)).uploadFile(any(), any(), any());
        verify(userAccountRepository, times(1)).save(any());

        assertEquals(modifiedUser, result);
    }

    @Test
    void shouldReturnUnchangedUserWhenNoChanges() {
        // given
        var currentUser = createUserAccount(1);
        var modifiedUserAuth0Id = currentUser.getAuth0Id();
        var modifiedUser = new UserAccount();

        when(userAccountRepository.findUserAccountByAuth0Id(modifiedUserAuth0Id))
                .thenReturn(Optional.of(currentUser));
        when(userAccountRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = userAccountServiceImpl.update(modifiedUserAuth0Id, modifiedUser, null);

        // then
        verify(userAccountRepository, times(1)).findUserAccountByAuth0Id(modifiedUserAuth0Id);
        verify(userAccountRepository, times(1)).save(any());

        assertEquals(currentUser, result);
    }
}
