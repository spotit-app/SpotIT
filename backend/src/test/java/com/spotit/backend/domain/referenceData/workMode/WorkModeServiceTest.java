package com.spotit.backend.domain.referenceData.workMode;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.createWorkMode;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.generateWorkModeList;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class WorkModeServiceTest {

    @Mock
    WorkModeRepository workModeRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    WorkModeServiceImpl workModeServiceImpl;

    @Test
    void shouldReturnAllWorkModes() {
        // given
        var foundWorkMode = generateWorkModeList(3);
        when(workModeRepository.findAll()).thenReturn(foundWorkMode);

        // when
        var result = workModeServiceImpl.getAll();

        // then
        verify(workModeRepository, times(1)).findAll();

        assertEquals(3, result.size());
        assertEquals(foundWorkMode.get(0), result.get(0));
        assertEquals(foundWorkMode.get(1), result.get(1));
        assertEquals(foundWorkMode.get(2), result.get(2));
    }

    @Test
    void shouldReturnWorkModeById() {
        // given
        var foundworkMode = createWorkMode(1);

        when(workModeRepository.findById(1)).thenReturn(Optional.of(foundworkMode));

        // when
        var result = workModeServiceImpl.getById(1);

        // then
        verify(workModeRepository, times(1)).findById(1);

        assertEquals(foundworkMode, result);
    }

    @Test
    void shouldReturnWorkModeByName() {
        // given
        var foundWorkMode = createWorkMode(1);
        var foundWorkModeName = foundWorkMode.getName();

        when(workModeRepository.findByName(foundWorkModeName)).thenReturn(Optional.of(foundWorkMode));

        // when
        var result = workModeServiceImpl.getByName(foundWorkModeName);

        // then
        verify(workModeRepository, times(1)).findByName(foundWorkModeName);

        assertEquals(foundWorkMode, result.get());
    }

    @Test
    void shouldThrowExceptionWhenWorkModeNotFound() {
        // given
        when(workModeRepository.findById(1)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(EntityNotFoundException.class, () -> workModeServiceImpl.getById(1));

        // then
        verify(workModeRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnWorkModeWhenCreatedSuccessfully() {
        // given
        var workModeToCreate = createWorkMode(1);

        when(workModeRepository.save(workModeToCreate))
                .thenReturn(workModeToCreate);

        // when
        var result = workModeServiceImpl.create(workModeToCreate);

        // then
        verify(workModeRepository, times(1)).save(workModeToCreate);

        assertEquals(workModeToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingworkMode() {
        // given
        var workModeToCreate = createWorkMode(1);

        when(workModeRepository.save(workModeToCreate)).thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(ErrorCreatingEntityException.class,
                () -> workModeServiceImpl.create(workModeToCreate));

        // then
        verify(workModeRepository, times(1)).save(workModeToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteWorkModeById() {
        // given
        var workModeToDeleteId = 1;
        var workModeToDelete = createWorkMode(workModeToDeleteId);

        when(workModeRepository.findById(workModeToDeleteId))
                .thenReturn(Optional.of(workModeToDelete));

        // when
        workModeServiceImpl.delete(workModeToDeleteId);

        // then
        verify(workModeRepository, times(workModeToDeleteId)).delete(workModeToDelete);
    }

    @Test
    void shouldReturnModifiedworkMode() {
        // given
        var workModeToModifyId = 1;
        var currentWorkMode = createWorkMode(workModeToModifyId);
        var modifiedWorkMode = createWorkMode(2);

        when(workModeRepository.findById(workModeToModifyId))
                .thenReturn(Optional.of(currentWorkMode));
        when(workModeRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workModeServiceImpl.update(workModeToModifyId, modifiedWorkMode);

        // then
        verify(workModeRepository, times(1)).findById(workModeToModifyId);
        verify(workModeRepository, times(1)).save(currentWorkMode);

        assertEquals(modifiedWorkMode, result);
    }

    @Test
    void shouldReturnPartiallyModifiedworkMode() {
        // given
        var workExprerienceId = 1;
        var currentWorkMode = createWorkMode(workExprerienceId);
        var modifiedWorkMode = WorkMode.builder().id(workExprerienceId).name("modifiedName").build();

        when(workModeRepository.findById(1)).thenReturn(Optional.of(currentWorkMode));
        when(workModeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workModeServiceImpl.update(1, modifiedWorkMode);

        // then
        verify(workModeRepository, times(1)).findById(1);
        verify(workModeRepository, times(1)).save(any());

        assertEquals(modifiedWorkMode.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedworkModeWhenNoChanges() {
        // given
        var workExprerienceId = 1;
        var currentworkMode = createWorkMode(workExprerienceId);
        var modifiedworkMode = new WorkMode();

        when(workModeRepository.findById(workExprerienceId)).thenReturn(Optional.of(currentworkMode));
        when(workModeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workModeServiceImpl.update(workExprerienceId, modifiedworkMode);

        // then
        verify(workModeRepository, times(1)).findById(workExprerienceId);
        verify(workModeRepository, times(1)).save(any());

        assertEquals(currentworkMode, result);
    }
}
