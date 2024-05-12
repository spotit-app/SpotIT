package com.spotit.backend.domain.referenceData.softSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.generateSoftSkillNameList;
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
class SoftSkillNameServiceTest {

    @Mock
    SoftSkillNameRepository softSkillNameRepository;

    @InjectMocks
    SoftSkillNameServiceImpl softSkillNameServiceImpl;

    @Test
    void shouldReturnAllSoftSkillNames() {
        // given
        var foundSoftSkillNames = generateSoftSkillNameList(3);

        when(softSkillNameRepository.findByCustom(false)).thenReturn(foundSoftSkillNames);

        // when
        var result = softSkillNameServiceImpl.getAll();

        // then
        verify(softSkillNameRepository, times(1)).findByCustom(false);

        assertEquals(3, result.size());
        assertEquals(foundSoftSkillNames.get(0), result.get(0));
        assertEquals(foundSoftSkillNames.get(1), result.get(1));
        assertEquals(foundSoftSkillNames.get(2), result.get(2));
    }

    @Test
    void shouldReturnSoftSkillNameById() {
        // given
        var foundSoftSkillNameId = 1;
        var foundSoftSkillName = createSoftSkillName(1);

        when(softSkillNameRepository.findById(foundSoftSkillNameId))
                .thenReturn(Optional.of(foundSoftSkillName));

        // when
        var result = softSkillNameServiceImpl.getById(foundSoftSkillNameId);

        // then
        verify(softSkillNameRepository, times(1)).findById(foundSoftSkillNameId);

        assertEquals(foundSoftSkillName, result);
    }

    @Test
    void shouldReturnSoftSkillNameByName() {
        // given
        var foundSoftSkillName = createSoftSkillName(1);
        var foundSoftSkillNameName = foundSoftSkillName.getName();

        when(softSkillNameRepository.findByName(foundSoftSkillNameName))
                .thenReturn(Optional.of(foundSoftSkillName));

        // when
        var result = softSkillNameServiceImpl.getByName(foundSoftSkillNameName);

        // then
        verify(softSkillNameRepository, times(1)).findByName(foundSoftSkillNameName);

        assertEquals(foundSoftSkillName, result.get());
    }

    @Test
    void shouldThrowExceptionWhenSoftSkillNameNotFound() {
        // given
        var foundSoftSkillNameId = 1;

        when(softSkillNameRepository.findById(foundSoftSkillNameId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> softSkillNameServiceImpl.getById(foundSoftSkillNameId));

        // then
        verify(softSkillNameRepository, times(1)).findById(foundSoftSkillNameId);

        assertEquals(getEntityNotFoundMessage(foundSoftSkillNameId), exception.getMessage());
    }

    @Test
    void shouldReturnSoftSkillNameWhenCreatedSuccessfully() {
        // given
        var softSkillNameToCreate = createSoftSkillName(1);

        when(softSkillNameRepository.save(softSkillNameToCreate))
                .thenReturn(softSkillNameToCreate);

        // when
        var result = softSkillNameServiceImpl.create(softSkillNameToCreate);

        // then
        verify(softSkillNameRepository, times(1)).save(softSkillNameToCreate);

        assertEquals(softSkillNameToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingSoftSkillName() {
        // given
        var softSkillNameToCreate = createSoftSkillName(1);

        when(softSkillNameRepository.save(softSkillNameToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> softSkillNameServiceImpl.create(softSkillNameToCreate));

        // then
        verify(softSkillNameRepository, times(1)).save(softSkillNameToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteSoftSkillNameById() {
        // given
        var softSkillNameId = 1;
        var softSkillNameToDelete = createSoftSkillName(softSkillNameId);

        when(softSkillNameRepository.findById(softSkillNameId))
                .thenReturn(Optional.of(softSkillNameToDelete));

        // when
        softSkillNameServiceImpl.delete(softSkillNameId);

        // then
        verify(softSkillNameRepository, times(1)).delete(softSkillNameToDelete);
    }

    @Test
    void shouldReturnModifiedSoftSkillName() {
        // given
        var softSkillNameId = 1;
        var currentSoftSkillName = createSoftSkillName(softSkillNameId);
        var modifiedSoftSkillName = createSoftSkillName(2);

        when(softSkillNameRepository.findById(softSkillNameId))
                .thenReturn(Optional.of(currentSoftSkillName));
        when(softSkillNameRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillNameServiceImpl.update(softSkillNameId, modifiedSoftSkillName);

        // then
        verify(softSkillNameRepository, times(1)).findById(softSkillNameId);
        verify(softSkillNameRepository, times(1)).save(currentSoftSkillName);

        assertEquals(modifiedSoftSkillName, result);
    }

    @Test
    void shouldReturnPartiallyModifiedSoftSkillName() {
        // given
        var softSkillNameId = 1;
        var currentSoftSkillName = createSoftSkillName(softSkillNameId);
        var modifiedSoftSkillName = SoftSkillName.builder()
                .id(softSkillNameId)
                .name("modifiedName")
                .build();

        when(softSkillNameRepository.findById(softSkillNameId)).thenReturn(Optional.of(currentSoftSkillName));
        when(softSkillNameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillNameServiceImpl.update(softSkillNameId, modifiedSoftSkillName);

        // then
        verify(softSkillNameRepository, times(1)).findById(softSkillNameId);
        verify(softSkillNameRepository, times(1)).save(any());

        assertEquals(modifiedSoftSkillName.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedSoftSkillNameWhenNoChanges() {
        // given
        var softSkillNameId = 1;
        var currentSoftSkillName = createSoftSkillName(softSkillNameId);
        var modifiedSoftSkillName = new SoftSkillName();

        when(softSkillNameRepository.findById(softSkillNameId))
                .thenReturn(Optional.of(currentSoftSkillName));
        when(softSkillNameRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillNameServiceImpl.update(softSkillNameId, modifiedSoftSkillName);

        // then
        verify(softSkillNameRepository, times(1)).findById(softSkillNameId);
        verify(softSkillNameRepository, times(1)).save(any());

        assertEquals(currentSoftSkillName, result);
    }
}
