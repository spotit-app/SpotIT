package com.spotit.backend.domain.referenceData.educationLevel;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.educationLevel.EducationLevelUtils.createEducationLevel;
import static com.spotit.backend.domain.referenceData.educationLevel.EducationLevelUtils.generateEducationLevelList;
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

@ExtendWith(MockitoExtension.class)
class EducationLevelServiceTest {

    @Mock
    EducationLevelRepository educationLevelRepository;

    @InjectMocks
    EducationLevelServiceImpl educationLevelServiceImpl;

    @Test
    void shouldReturnAllEducationLevels() {
        // given
        var foundEducationLevels = generateEducationLevelList(3);

        when(educationLevelRepository.findByCustom(false)).thenReturn(foundEducationLevels);

        // when
        var result = educationLevelServiceImpl.getAll();

        // then
        verify(educationLevelRepository, times(1)).findByCustom(false);

        assertEquals(3, result.size());
        assertEquals(foundEducationLevels.get(0), result.get(0));
        assertEquals(foundEducationLevels.get(1), result.get(1));
        assertEquals(foundEducationLevels.get(2), result.get(2));
    }

    @Test
    void shouldReturnEducationLevelById() {
        // given
        var foundEducationLevelId = 1;
        var foundEducationLevel = createEducationLevel(1);

        when(educationLevelRepository.findById(foundEducationLevelId))
                .thenReturn(Optional.of(foundEducationLevel));

        // when
        var result = educationLevelServiceImpl.getById(foundEducationLevelId);

        // then
        verify(educationLevelRepository, times(1)).findById(foundEducationLevelId);

        assertEquals(foundEducationLevel, result);
    }

    @Test
    void shouldReturnEducationLevelByName() {
        // given
        var foundEducationLevel = createEducationLevel(1);
        var foundEducationLevelName = foundEducationLevel.getName();

        when(educationLevelRepository.findByName(foundEducationLevelName))
                .thenReturn(Optional.of(foundEducationLevel));

        // when
        var result = educationLevelServiceImpl.getByName(foundEducationLevelName);

        // then
        verify(educationLevelRepository, times(1)).findByName(foundEducationLevelName);

        assertEquals(foundEducationLevel, result.get());
    }

    @Test
    void shouldThrowExceptionWhenEducationLevelNotFound() {
        // given
        var foundEducationLevelId = 1;

        when(educationLevelRepository.findById(foundEducationLevelId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> educationLevelServiceImpl.getById(foundEducationLevelId));

        // then
        verify(educationLevelRepository, times(1)).findById(foundEducationLevelId);

        assertEquals(getEntityNotFoundMessage(foundEducationLevelId), exception.getMessage());
    }

    @Test
    void shouldReturnEducationLevelWhenCreatedSuccessfully() {
        // given
        var educationLevelToCreate = createEducationLevel(1);

        when(educationLevelRepository.save(educationLevelToCreate))
                .thenReturn(educationLevelToCreate);

        // when
        var result = educationLevelServiceImpl.create(educationLevelToCreate);

        // then
        verify(educationLevelRepository, times(1)).save(educationLevelToCreate);

        assertEquals(educationLevelToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingEducationLevel() {
        // given
        var educationLevelToCreate = createEducationLevel(1);

        when(educationLevelRepository.save(educationLevelToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> educationLevelServiceImpl.create(educationLevelToCreate));

        // then
        verify(educationLevelRepository, times(1)).save(educationLevelToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteEducationLevelById() {
        // given
        var educationLevelId = 1;
        var educationLevelToDelete = createEducationLevel(educationLevelId);

        when(educationLevelRepository.findById(educationLevelId))
                .thenReturn(Optional.of(educationLevelToDelete));

        // when
        educationLevelServiceImpl.delete(educationLevelId);

        // then
        verify(educationLevelRepository, times(1)).delete(educationLevelToDelete);
    }

    @Test
    void shouldReturnModifiedEducationLevel() {
        // given
        var educationLevelId = 1;
        var currentEducationLevel = createEducationLevel(educationLevelId);
        var modifiedEducationLevel = createEducationLevel(2);

        when(educationLevelRepository.findById(educationLevelId))
                .thenReturn(Optional.of(currentEducationLevel));
        when(educationLevelRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationLevelServiceImpl.update(educationLevelId, modifiedEducationLevel);

        // then
        verify(educationLevelRepository, times(1)).findById(educationLevelId);
        verify(educationLevelRepository, times(1)).save(currentEducationLevel);

        assertEquals(modifiedEducationLevel, result);
    }

    @Test
    void shouldReturnPartiallyModifiedEducationLevel() {
        // given
        var educationLevelId = 1;
        var currentEducationLevel = createEducationLevel(educationLevelId);
        var modifiedEducationLevel = EducationLevel.builder()
                .id(educationLevelId)
                .name("modifiedName")
                .build();

        when(educationLevelRepository.findById(educationLevelId))
                .thenReturn(Optional.of(currentEducationLevel));
        when(educationLevelRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationLevelServiceImpl.update(educationLevelId, modifiedEducationLevel);

        // then
        verify(educationLevelRepository, times(1)).findById(educationLevelId);
        verify(educationLevelRepository, times(1)).save(any());

        assertEquals(modifiedEducationLevel.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedEducationLevelWhenNoChanges() {
        // given
        var educationLevelId = 1;
        var currentEducationLevel = createEducationLevel(educationLevelId);
        var modifiedEducationLevel = new EducationLevel();

        when(educationLevelRepository.findById(educationLevelId))
                .thenReturn(Optional.of(currentEducationLevel));
        when(educationLevelRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationLevelServiceImpl.update(educationLevelId, modifiedEducationLevel);

        // then
        verify(educationLevelRepository, times(1)).findById(educationLevelId);
        verify(educationLevelRepository, times(1)).save(any());

        assertEquals(currentEducationLevel, result);
    }
}
