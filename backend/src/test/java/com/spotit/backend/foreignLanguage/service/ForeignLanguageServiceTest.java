package com.spotit.backend.foreignLanguage.service;

import static com.spotit.backend.testUtils.ForeignLanguageUtils.createForeignLanguage;
import static com.spotit.backend.testUtils.ForeignLanguageUtils.generateForeignLanguageList;
import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.testUtils.UserAccountUtils.createUserAccount;
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

import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;
import com.spotit.backend.foreignLanguage.repository.ForeignLanguageRepository;
import com.spotit.backend.userAccount.service.UserAccountService;

@ExtendWith(MockitoExtension.class)
class ForeignLanguageServiceTest {

    @Mock
    ForeignLanguageRepository foreignLanguageRepository;

    @Mock
    UserAccountService userAccountService;

    @InjectMocks
    ForeignLanguageServiceImpl foreignLanguageServiceImpl;

    @Test
    void shouldReturnAllForeignLanguageOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundForeignLanguages = generateForeignLanguageList(3);

        when(foreignLanguageRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundForeignLanguages);

        // when
        var result = foreignLanguageServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(foreignLanguageRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(3, result.size());
        assertEquals(foundForeignLanguages.get(0), result.get(0));
        assertEquals(foundForeignLanguages.get(1), result.get(1));
        assertEquals(foundForeignLanguages.get(2), result.get(2));
    }

    @Test
    void shouldReturnForeignLanguageById() {
        // given
        var foreignLanguageId = 1;
        var foundForeignLanguage = createForeignLanguage(foreignLanguageId);

        when(foreignLanguageRepository.findById(foreignLanguageId))
                .thenReturn(Optional.of(foundForeignLanguage));

        // when
        var result = foreignLanguageServiceImpl.getById(foreignLanguageId);

        // then
        verify(foreignLanguageRepository, times(1)).findById(foreignLanguageId);

        assertEquals(foundForeignLanguage, result);
    }

    @Test
    void shouldThrowExceptionWhenForeignLanguageNotFound() {
        // given
        var foreignLanguageId = 1;

        when(foreignLanguageRepository.findById(foreignLanguageId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> foreignLanguageServiceImpl.getById(foreignLanguageId));

        // then
        verify(foreignLanguageRepository, times(1)).findById(foreignLanguageId);

        assertEquals(getEntityNotFoundMessage(foreignLanguageId), exception.getMessage());
    }

    @Test
    void shouldReturnForeignLanguageWhenCreatedSuccesfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var foreignLanguageToCreate = createForeignLanguage(1);

        when(foreignLanguageRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = foreignLanguageServiceImpl.create(userAccountAuth0Id, foreignLanguageToCreate);

        // then
        verify(foreignLanguageRepository, times(1)).save(foreignLanguageToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(foreignLanguageToCreate.getLanguageLevel(), result.getLanguageLevel());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingForeignLanguage() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var foreignLanguageToCreate = createForeignLanguage(1);

        when(foreignLanguageRepository.save(foreignLanguageToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> foreignLanguageServiceImpl.create(userAccountAuth0Id, foreignLanguageToCreate));

        // then
        verify(foreignLanguageRepository, times(1)).save(foreignLanguageToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteForeignLanguage() {
        // given
        var foreignLanguageToDeleteId = 1;
        var foreignLanguageToDelete = createForeignLanguage(foreignLanguageToDeleteId);

        when(foreignLanguageRepository.findById(foreignLanguageToDeleteId))
                .thenReturn(Optional.of(foreignLanguageToDelete));

        // when
        foreignLanguageServiceImpl.delete(foreignLanguageToDeleteId);

        // then
        verify(foreignLanguageRepository, times(1)).findById(foreignLanguageToDeleteId);
        verify(foreignLanguageRepository, times(1)).delete(foreignLanguageToDelete);
    }

    @Test
    void shouldReturnModifiedForeignLanguage() {
        // given
        var foreignLanguageToModifyId = 1;
        var currentForeignLanguage = createForeignLanguage(foreignLanguageToModifyId);
        var modifiedForeignLanguage = createForeignLanguage(2);

        when(foreignLanguageRepository.findById(foreignLanguageToModifyId))
                .thenReturn(Optional.of(currentForeignLanguage));
        when(foreignLanguageRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = foreignLanguageServiceImpl.update(foreignLanguageToModifyId, modifiedForeignLanguage);

        // then
        verify(foreignLanguageRepository, times(1)).findById(foreignLanguageToModifyId);
        verify(foreignLanguageRepository, times(1)).save(any());

        assertEquals(modifiedForeignLanguage.getLanguageLevel(), result.getLanguageLevel());
    }

    @Test
    void shouldReturnUnchangedForeignLanguageWhenNoChanges() {
        // given
        var foreignLanguageToModifyId = 1;
        var currentForeignLanguage = createForeignLanguage(foreignLanguageToModifyId);
        var modifiedForeignLanguage = new ForeignLanguage();

        when(foreignLanguageRepository.findById(foreignLanguageToModifyId))
                .thenReturn(Optional.of(currentForeignLanguage));
        when(foreignLanguageRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = foreignLanguageServiceImpl.update(foreignLanguageToModifyId, modifiedForeignLanguage);

        // then
        verify(foreignLanguageRepository, times(1)).findById(foreignLanguageToModifyId);
        verify(foreignLanguageRepository, times(1)).save(any());

        assertEquals(currentForeignLanguage, result);
    }
}
