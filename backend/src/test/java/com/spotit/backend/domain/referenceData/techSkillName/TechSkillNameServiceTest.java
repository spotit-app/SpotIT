package com.spotit.backend.domain.referenceData.techSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.createTechSkillName;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.generateTechSkillNameList;
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
class TechSkillNameServiceTest {

    @Mock
    TechSkillNameRepository techSkillNameRepository;

    @Mock
    StorageService storageService;

    @InjectMocks
    TechSkillNameServiceImpl techSkillNameServiceImpl;

    @Test
    void shouldReturnAllTechSkillNames() {
        // given
        var foundTechSkillNames = generateTechSkillNameList(3);
        when(techSkillNameRepository.findByCustom(false)).thenReturn(foundTechSkillNames);

        // when
        var result = techSkillNameServiceImpl.getAll();

        // then
        verify(techSkillNameRepository, times(1)).findByCustom(false);

        assertEquals(3, result.size());
        assertEquals(foundTechSkillNames.get(0), result.get(0));
        assertEquals(foundTechSkillNames.get(1), result.get(1));
        assertEquals(foundTechSkillNames.get(2), result.get(2));
    }

    @Test
    void shouldReturnTechSkillNameById() {
        // given
        var foundTechSkillName = createTechSkillName(1);

        when(techSkillNameRepository.findById(1)).thenReturn(Optional.of(foundTechSkillName));

        // when
        var result = techSkillNameServiceImpl.getById(1);

        // then
        verify(techSkillNameRepository, times(1)).findById(1);

        assertEquals(foundTechSkillName, result);
    }

    @Test
    void shouldReturnTechSkillNameByName() {
        // given
        var foundTechSkillName = createTechSkillName(1);
        var foundTechSkillNameName = foundTechSkillName.getName();

        when(techSkillNameRepository.findByName(foundTechSkillNameName)).thenReturn(Optional.of(foundTechSkillName));

        // when
        var result = techSkillNameServiceImpl.getByName(foundTechSkillNameName);

        // then
        verify(techSkillNameRepository, times(1)).findByName(foundTechSkillNameName);

        assertEquals(foundTechSkillName, result.get());
    }

    @Test
    void shouldThrowExceptionWhenTechSkillNameNotFound() {
        // given
        when(techSkillNameRepository.findById(1)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(EntityNotFoundException.class, () -> techSkillNameServiceImpl.getById(1));

        // then
        verify(techSkillNameRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnTechSkillNameWhenCreatedSuccessfully() {
        // given
        var techSkillNameName = "Name 1";
        var techSkillNameLogo = new byte[0];

        when(storageService.uploadFile(techSkillNameLogo, "logos", techSkillNameName + "-logo")).thenReturn("logoUrl");
        when(techSkillNameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillNameServiceImpl.create(techSkillNameName, techSkillNameLogo);

        // then
        verify(storageService, times(1)).uploadFile(techSkillNameLogo, "logos", techSkillNameName + "-logo");
        verify(techSkillNameRepository, times(1)).save(any());

        assertEquals(techSkillNameName, result.getName());
        assertEquals("logoUrl", result.getLogoUrl());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingTechSkillName() {
        // given
        var techSkillNameName = "Name 1";
        var techSkillNameLogo = new byte[0];

        when(storageService.uploadFile(techSkillNameLogo, "logos", techSkillNameName + "-logo")).thenReturn("logoUrl");
        when(techSkillNameRepository.save(any())).thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(ErrorCreatingEntityException.class,
                () -> techSkillNameServiceImpl.create(techSkillNameName, techSkillNameLogo));

        // then
        verify(storageService, times(1)).uploadFile(techSkillNameLogo, "logos", techSkillNameName + "-logo");
        verify(techSkillNameRepository, times(1)).save(any());

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteTechSkillNameById() {
        // given
        var techSkillNameToDeleteId = 1;
        var techSkillNameToDelete = createTechSkillName(1);

        when(techSkillNameRepository.findById(1)).thenReturn(Optional.of(techSkillNameToDelete));

        // when
        techSkillNameServiceImpl.delete(1);

        // then
        verify(techSkillNameRepository, times(1)).findById(techSkillNameToDeleteId);
        verify(storageService, times(1)).deleteFile(any());
        verify(techSkillNameRepository, times(1)).delete(techSkillNameToDelete);
    }

    @Test
    void shouldReturnModifiedTechSkillName() {
        // given
        var techSkillNameToModifyId = 1;
        var currentTechSkillName = createTechSkillName(techSkillNameToModifyId);
        var modifiedTechSkillNameName = "Name 2";
        var modifiedTechSkillNameLogo = new byte[0];

        when(techSkillNameRepository.findById(techSkillNameToModifyId)).thenReturn(Optional.of(currentTechSkillName));
        when(techSkillNameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillNameServiceImpl.update(techSkillNameToModifyId, modifiedTechSkillNameName,
                modifiedTechSkillNameLogo);

        // then
        verify(techSkillNameRepository, times(1)).findById(techSkillNameToModifyId);
        verify(storageService, times(1)).uploadFile(any(), any(), any());
        verify(techSkillNameRepository, times(1)).save(any());

        assertEquals(modifiedTechSkillNameName, result.getName());
    }

    @Test
    void shouldReturnPartiallyModifiedTechSkillName() {
        // given
        var currentTechSkillName = createTechSkillName(1);
        var modifiedTechSkillName = TechSkillName.builder().id(1).name("modifiedName").build();

        when(techSkillNameRepository.findById(1)).thenReturn(Optional.of(currentTechSkillName));
        when(techSkillNameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillNameServiceImpl.update(1, modifiedTechSkillName);

        // then
        verify(techSkillNameRepository, times(1)).findById(1);
        verify(techSkillNameRepository, times(1)).save(any());

        assertEquals(modifiedTechSkillName.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedTechSkillNameWhenNoChanges() {
        // given
        var currentTechSkillName = createTechSkillName(1);

        when(techSkillNameRepository.findById(1)).thenReturn(Optional.of(currentTechSkillName));
        when(techSkillNameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillNameServiceImpl.update(1, null, null);

        // then
        verify(techSkillNameRepository, times(1)).findById(1);
        verify(techSkillNameRepository, times(1)).save(any());

        assertEquals(currentTechSkillName, result);
    }
}
