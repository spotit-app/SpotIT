package com.spotit.backend.employee.referenceData.foreignLanguageName;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.referenceData.foreignLanguageName.ForeignLanguageNameUtils.createForeignLanguageName;
import static com.spotit.backend.employee.referenceData.foreignLanguageName.ForeignLanguageNameUtils.generateForeignLanguageNameList;
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
import com.spotit.backend.storage.StorageService;

@ExtendWith(MockitoExtension.class)
class ForeignLanguageNameServiceTest {

    @Mock
    ForeignLanguageNameRepository foreignLanguageNameRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    ForeignLanguageNameServiceImpl foreignLanguageNameServiceImpl;

    @Test
    void shouldReturnAllForeignLanguages() {
        // given
        var foundProjects = generateForeignLanguageNameList(3);

        when(foreignLanguageNameRepository.findAll())
                .thenReturn(foundProjects);

        // when
        var result = foreignLanguageNameServiceImpl.getAll();

        // then
        verify(foreignLanguageNameRepository, times(1)).findAll();

        assertEquals(3, result.size());
        assertEquals(foundProjects.get(0), result.get(0));
        assertEquals(foundProjects.get(1), result.get(1));
        assertEquals(foundProjects.get(2), result.get(2));
    }

    @Test
    void shouldReturnForeignLanguageNameById() {
        // given
        var foreignLanguageNameId = 1;
        var foundForeignLanguageName = createForeignLanguageName(foreignLanguageNameId);

        when(foreignLanguageNameRepository.findById(foreignLanguageNameId))
                .thenReturn(Optional.of(foundForeignLanguageName));

        // when
        var result = foreignLanguageNameServiceImpl.getById(foreignLanguageNameId);

        // then
        verify(foreignLanguageNameRepository, times(1)).findById(foreignLanguageNameId);

        assertEquals(foundForeignLanguageName, result);
    }

    @Test
    void shouldThrowExceptionWhenForeignLanguageNameNotFound() {
        // given
        var foreignLanguageNameId = 1;

        when(foreignLanguageNameRepository.findById(foreignLanguageNameId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> foreignLanguageNameServiceImpl.getById(foreignLanguageNameId));

        // then
        verify(foreignLanguageNameRepository, times(1)).findById(foreignLanguageNameId);

        assertEquals(getEntityNotFoundMessage(foreignLanguageNameId), exception.getMessage());
    }

    @Test
    void shouldReturnForeignLanguageNameWhenCreatedSuccesfully() {
        // given
        var foreignLanguageNameToCreate = "Name1";
        var foreignLanguageNameFlag = new byte[0];

        when(storageService.uploadFile(
                foreignLanguageNameFlag,
                "flags",
                foreignLanguageNameToCreate + "-flag"))
                .thenReturn("flagUrl");
        when(foreignLanguageNameRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = foreignLanguageNameServiceImpl.create(
                foreignLanguageNameToCreate,
                foreignLanguageNameFlag);

        // then
        verify(storageService, times(1)).uploadFile(
                foreignLanguageNameFlag,
                "flags",
                foreignLanguageNameToCreate + "-flag");
        verify(foreignLanguageNameRepository, times(1)).save(any());

        assertEquals(foreignLanguageNameToCreate, result.getName());
        assertEquals("flagUrl", result.getFlagUrl());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingForeignLanguageName() {
        // given
        var foreignLanguageNameToCreate = "Name1";
        var foreignLanguageNameFlag = new byte[0];

        when(storageService.uploadFile(
                foreignLanguageNameFlag,
                "flags",
                foreignLanguageNameToCreate + "-flag"))
                .thenReturn("flagUrl");
        when(foreignLanguageNameRepository.save(any()))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> foreignLanguageNameServiceImpl.create(
                        foreignLanguageNameToCreate,
                        foreignLanguageNameFlag));

        // then
        verify(storageService, times(1)).uploadFile(
                foreignLanguageNameFlag,
                "flags",
                foreignLanguageNameToCreate + "-flag");
        verify(foreignLanguageNameRepository, times(1)).save(any());

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteForeignLanguageName() {
        // given
        var foreignLanguageNameToDeleteId = 1;
        var foreignLanguageNameToDelete = createForeignLanguageName(foreignLanguageNameToDeleteId);

        when(foreignLanguageNameRepository.findById(foreignLanguageNameToDeleteId))
                .thenReturn(Optional.of(foreignLanguageNameToDelete));

        // when
        foreignLanguageNameServiceImpl.delete(foreignLanguageNameToDeleteId);

        // then
        verify(foreignLanguageNameRepository, times(1))
                .findById(foreignLanguageNameToDeleteId);
        verify(storageService, times(1))
                .deleteFile(any());
        verify(foreignLanguageNameRepository, times(1))
                .delete(foreignLanguageNameToDelete);
    }

    @Test
    void shouldReturnModifiedForeignLanguageName() {
        // given
        var foreignLanguageNameToModifyId = 1;
        var currentForeignLanguageName = createForeignLanguageName(foreignLanguageNameToModifyId);
        var modifiedForeignLanguageName = "Name2";
        var modifiedForeignLanguageFlag = new byte[0];

        when(foreignLanguageNameRepository.findById(foreignLanguageNameToModifyId))
                .thenReturn(Optional.of(currentForeignLanguageName));
        when(foreignLanguageNameRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = foreignLanguageNameServiceImpl.update(
                foreignLanguageNameToModifyId,
                modifiedForeignLanguageName,
                modifiedForeignLanguageFlag);

        // then
        verify(foreignLanguageNameRepository, times(1))
                .findById(foreignLanguageNameToModifyId);
        verify(storageService, times(1))
                .uploadFile(any(), any(), any());
        verify(foreignLanguageNameRepository, times(1))
                .save(any());

        assertEquals(modifiedForeignLanguageName, result.getName());
    }

    @Test
    void shouldReturnUnchangedForeignLanguageNameWhenNoChanges() {
        // given
        var foreignLanguageNameToModifyId = 1;
        var currentForeignLanguageName = createForeignLanguageName(foreignLanguageNameToModifyId);

        when(foreignLanguageNameRepository.findById(foreignLanguageNameToModifyId))
                .thenReturn(Optional.of(currentForeignLanguageName));
        when(foreignLanguageNameRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = foreignLanguageNameServiceImpl.update(
                foreignLanguageNameToModifyId,
                null,
                null);

        // then
        verify(foreignLanguageNameRepository, times(1)).findById(foreignLanguageNameToModifyId);
        verify(foreignLanguageNameRepository, times(1)).save(any());

        assertEquals(currentForeignLanguageName, result);
    }
}
