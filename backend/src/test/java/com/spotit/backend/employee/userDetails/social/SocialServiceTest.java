package com.spotit.backend.employee.userDetails.social;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.social.SocialUtils.createSocial;
import static com.spotit.backend.employee.userDetails.social.SocialUtils.generateSocialsList;
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

import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountService;

@ExtendWith(MockitoExtension.class)
class SocialServiceTest {

    @Mock
    SocialRepository socialRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    SocialServiceImpl socialServiceImpl;

    @Test
    void shouldReturnAllSocialsOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundSocials = generateSocialsList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(socialRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundSocials);

        // when
        var result = socialServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(socialRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(foundSocials.size(), result.size());
        assertEquals(foundSocials.get(0), result.get(0));
        assertEquals(foundSocials.get(1), result.get(1));
        assertEquals(foundSocials.get(2), result.get(2));
    }

    @Test
    void shouldReturnSocialById() {
        // given
        var socialId = 1;
        var foundSocial = createSocial(socialId);

        when(socialRepository.findById(socialId))
                .thenReturn(Optional.of(foundSocial));

        // when
        var result = socialServiceImpl.getById(socialId);

        // then
        verify(socialRepository, times(1)).findById(socialId);

        assertEquals(foundSocial, result);
    }

    @Test
    void shouldThrowExceptionWhenSocialNotFound() {
        // given
        var socialId = 1;

        when(socialRepository.findById(socialId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> socialServiceImpl.getById(socialId));

        // then
        verify(socialRepository, times(1)).findById(socialId);

        assertEquals(getEntityNotFoundMessage(socialId), exception.getMessage());
    }

    @Test
    void shouldReturnSocialWhenCreatedSuccesfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var socialToCreate = createSocial(1);

        when(socialRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = socialServiceImpl.create(userAccountAuth0Id, socialToCreate);

        // then
        verify(socialRepository, times(1)).save(socialToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(socialToCreate.getName(), result.getName());
        assertEquals(socialToCreate.getSocialUrl(), result.getSocialUrl());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingSocial() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var socialToCreate = createSocial(1);

        when(socialRepository.save(socialToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> socialServiceImpl.create(userAccountAuth0Id, socialToCreate));

        // then
        verify(socialRepository, times(1)).save(socialToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteSocial() {
        // given
        var socialToDeleteId = 1;
        var socialToDelete = createSocial(socialToDeleteId);

        when(socialRepository.findById(socialToDeleteId))
                .thenReturn(Optional.of(socialToDelete));

        // when
        socialServiceImpl.delete(socialToDeleteId);

        // then
        verify(socialRepository, times(1)).findById(socialToDeleteId);
        verify(socialRepository, times(1)).delete(socialToDelete);
    }

    @Test
    void shouldReturnModifiedSocial() {
        // given
        var socialToModifyId = 1;
        var currentSocial = createSocial(socialToModifyId);
        var modifiedSocial = createSocial(2);

        when(socialRepository.findById(socialToModifyId))
                .thenReturn(Optional.of(currentSocial));
        when(socialRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = socialServiceImpl.update(socialToModifyId, modifiedSocial);

        // then
        verify(socialRepository, times(1)).findById(socialToModifyId);
        verify(socialRepository, times(1)).save(any());

        assertEquals(modifiedSocial.getName(), result.getName());
        assertEquals(modifiedSocial.getSocialUrl(), result.getSocialUrl());
    }

    @Test
    void shouldReturnUnchangedSocialWhenNoChanges() {
        // given
        var socialToModifyId = 1;
        var currentSocial = createSocial(socialToModifyId);
        var modifiedSocial = new Social();

        when(socialRepository.findById(socialToModifyId))
                .thenReturn(Optional.of(currentSocial));
        when(socialRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = socialServiceImpl.update(socialToModifyId, modifiedSocial);

        // then
        verify(socialRepository, times(1)).findById(socialToModifyId);
        verify(socialRepository, times(1)).save(any());

        assertEquals(currentSocial, result);
    }
}
