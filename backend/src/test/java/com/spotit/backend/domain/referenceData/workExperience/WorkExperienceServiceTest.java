package com.spotit.backend.domain.referenceData.workExperience;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperience;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.generateWorkExperienceList;
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
class WorkExperienceServiceTest {

    @Mock
    WorkExperienceRepository workExperienceRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    WorkExperienceServiceImpl workExperienceServiceImpl;

    @Test
    void shouldReturnAllWorkExperiences() {
        // given
        var foundWorkExperience = generateWorkExperienceList(3);
        when(workExperienceRepository.findAll()).thenReturn(foundWorkExperience);

        // when
        var result = workExperienceServiceImpl.getAll();

        // then
        verify(workExperienceRepository, times(1)).findAll();

        assertEquals(3, result.size());
        assertEquals(foundWorkExperience.get(0), result.get(0));
        assertEquals(foundWorkExperience.get(1), result.get(1));
        assertEquals(foundWorkExperience.get(2), result.get(2));
    }

    @Test
    void shouldReturnWorkExperienceById() {
        // given
        var foundWorkExperience = createWorkExperience(1);

        when(workExperienceRepository.findById(1)).thenReturn(Optional.of(foundWorkExperience));

        // when
        var result = workExperienceServiceImpl.getById(1);

        // then
        verify(workExperienceRepository, times(1)).findById(1);

        assertEquals(foundWorkExperience, result);
    }

    @Test
    void shouldReturnWorkExperienceByName() {
        // given
        var foundWorkExperience = createWorkExperience(1);
        var foundWorkExperienceName = foundWorkExperience.getName();

        when(workExperienceRepository.findByName(foundWorkExperienceName)).thenReturn(Optional.of(foundWorkExperience));

        // when
        var result = workExperienceServiceImpl.getByName(foundWorkExperienceName);

        // then
        verify(workExperienceRepository, times(1)).findByName(foundWorkExperienceName);

        assertEquals(foundWorkExperience, result.get());
    }

    @Test
    void shouldThrowExceptionWhenWorkExperienceNotFound() {
        // given
        when(workExperienceRepository.findById(1)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(EntityNotFoundException.class, () -> workExperienceServiceImpl.getById(1));

        // then
        verify(workExperienceRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnWorkExperienceWhenCreatedSuccessfully() {
        // given
        var workExperienceToCreate = createWorkExperience(1);

        when(workExperienceRepository.save(workExperienceToCreate))
                .thenReturn(workExperienceToCreate);

        // when
        var result = workExperienceServiceImpl.create(workExperienceToCreate);

        // then
        verify(workExperienceRepository, times(1)).save(workExperienceToCreate);

        assertEquals(workExperienceToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingWorkExperience() {
        // given
        var workExperienceToCreate = createWorkExperience(1);

        when(workExperienceRepository.save(workExperienceToCreate)).thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(ErrorCreatingEntityException.class,
                () -> workExperienceServiceImpl.create(workExperienceToCreate));

        // then
        verify(workExperienceRepository, times(1)).save(workExperienceToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteWorkExperienceById() {
        // given
        var workExperienceToDeleteId = 1;
        var workExperienceToDelete = createWorkExperience(workExperienceToDeleteId);

        when(workExperienceRepository.findById(workExperienceToDeleteId))
                .thenReturn(Optional.of(workExperienceToDelete));

        // when
        workExperienceServiceImpl.delete(workExperienceToDeleteId);

        // then
        verify(workExperienceRepository, times(workExperienceToDeleteId)).delete(workExperienceToDelete);
    }

    @Test
    void shouldReturnModifiedWorkExperience() {
        // given
        var workExperienceToModifyId = 1;
        var currentworkExperience = createWorkExperience(workExperienceToModifyId);
        var modifiedworkExperience = createWorkExperience(2);

        when(workExperienceRepository.findById(workExperienceToModifyId))
                .thenReturn(Optional.of(currentworkExperience));
        when(workExperienceRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workExperienceServiceImpl.update(workExperienceToModifyId, modifiedworkExperience);

        // then
        verify(workExperienceRepository, times(1)).findById(workExperienceToModifyId);
        verify(workExperienceRepository, times(1)).save(currentworkExperience);

        assertEquals(modifiedworkExperience, result);
    }

    @Test
    void shouldReturnPartiallyModifiedWorkExperience() {
        // given
        var workExprerienceId = 1;
        var currentWorkExperience = createWorkExperience(workExprerienceId);
        var modifiedWorkExperience = WorkExperience.builder().id(workExprerienceId).name("modifiedName").build();

        when(workExperienceRepository.findById(1)).thenReturn(Optional.of(currentWorkExperience));
        when(workExperienceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workExperienceServiceImpl.update(1, modifiedWorkExperience);

        // then
        verify(workExperienceRepository, times(1)).findById(1);
        verify(workExperienceRepository, times(1)).save(any());

        assertEquals(modifiedWorkExperience.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedWorkExperienceWhenNoChanges() {
        // given
        var workExprerienceId = 1;
        var currentWorkExperience = createWorkExperience(workExprerienceId);
        var modifiedWorkExperience = new WorkExperience();

        when(workExperienceRepository.findById(workExprerienceId)).thenReturn(Optional.of(currentWorkExperience));
        when(workExperienceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = workExperienceServiceImpl.update(workExprerienceId, modifiedWorkExperience);

        // then
        verify(workExperienceRepository, times(1)).findById(workExprerienceId);
        verify(workExperienceRepository, times(1)).save(any());

        assertEquals(currentWorkExperience, result);
    }
}
