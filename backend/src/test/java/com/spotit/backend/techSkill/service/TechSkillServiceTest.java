package com.spotit.backend.techSkill.service;

import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.testUtils.TechSkillUtils.createTechSkill;
import static com.spotit.backend.testUtils.TechSkillUtils.generateTechSkillList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
import com.spotit.backend.techSkill.model.TechSkill;
import com.spotit.backend.techSkill.repository.TechSkillRepository;
import com.spotit.backend.userAccount.service.UserAccountService;

@ExtendWith(MockitoExtension.class)
public class TechSkillServiceTest {

    @Mock
    TechSkillRepository techSkillRepository;

    @Mock
    UserAccountService userAccountService;

    @InjectMocks
    TechSkillServiceImpl techSkillServiceImpl;

    @Test
    void shouldReturnAllTechSkills() {
        // given
        var auth0Id = "auth0Id1";
        var foundTechSkills = generateTechSkillList(3);

        when(techSkillRepository.findByUserAccountAuth0Id(auth0Id)).thenReturn(foundTechSkills);

        // when
        var result = techSkillServiceImpl.getAllByUserAccountAuth0Id(auth0Id);

        // then
        verify(techSkillRepository, times(1)).findByUserAccountAuth0Id(auth0Id);

        assertEquals(foundTechSkills.size(), result.size());
        assertEquals(foundTechSkills.get(0), result.get(0));
        assertEquals(foundTechSkills.get(1), result.get(1));
        assertEquals(foundTechSkills.get(2), result.get(2));
    }

    @Test
    void shouldReturnTechSkillById() {
        // given
        var foundTechSkill = createTechSkill(1);

        when(techSkillRepository.findById(1)).thenReturn(Optional.of(foundTechSkill));

        // when
        var result = techSkillServiceImpl.getById(1);

        // then
        verify(techSkillRepository, times(1)).findById(1);

        assertEquals(foundTechSkill, result);
    }

    @Test
    void shouldThrowExceptionWhenTechSkillNotFound() {
        // given
        when(techSkillRepository.findById(1))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> techSkillServiceImpl.getById(1));

        // then
        verify(techSkillRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnTechSkillWhenCreatedSuccessfully() {
        // given
        var auth0Id = "auth0Id1";
        var techSkillToCreate = createTechSkill(1);

        when(techSkillRepository.save(techSkillToCreate))
                .thenReturn(techSkillToCreate);

        // when
        var result = techSkillServiceImpl.create(auth0Id, techSkillToCreate);

        // then
        verify(techSkillRepository, times(1)).save(techSkillToCreate);

        assertEquals(techSkillToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingTechSkill() {
        // given
        var auth0Id = "auth0Id1";
        var techSkillToCreate = createTechSkill(1);

        when(techSkillRepository.save(techSkillToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> techSkillServiceImpl.create(auth0Id, techSkillToCreate));

        // then
        verify(techSkillRepository, times(1)).save(techSkillToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteTechSkillById() {
        // given
        var techSkillToDelete = createTechSkill(1);

        when(techSkillRepository.findById(1)).thenReturn(Optional.of(techSkillToDelete));

        // when
        techSkillServiceImpl.delete(1);

        // then
        verify(techSkillRepository, times(1)).findById(1);
        verify(techSkillRepository, times(1)).delete(techSkillToDelete);
    }

    @Test
    void shouldReturnModifiedTechSkill() {
        // given
        var currentTechSkill = createTechSkill(1);
        var modifiedTechSkill = createTechSkill(2);

        when(techSkillRepository.findById(1))
                .thenReturn(Optional.of(currentTechSkill));
        when(techSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillServiceImpl.update(1, modifiedTechSkill);

        // then
        verify(techSkillRepository, times(1)).findById(1);
        verify(techSkillRepository, times(1)).save(modifiedTechSkill);

        assertEquals(modifiedTechSkill, result);
    }

    @Test
    void shouldReturnPartiallyModifiedTechSkill() {
        // given
        var currentTechSkill = createTechSkill(1);
        var modifiedTechSkill = TechSkill.builder()
                .id(currentTechSkill.getId())
                .skillLevel(5)
                .build();

        when(techSkillRepository.findById(1))
                .thenReturn(Optional.of(currentTechSkill));
        when(techSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillServiceImpl.update(1, modifiedTechSkill);

        // then
        verify(techSkillRepository, times(1)).findById(1);
        verify(techSkillRepository, times(1)).save(modifiedTechSkill);

        assertEquals(modifiedTechSkill, result);
    }

    @Test
    void shouldReturnUnchangedTechSkillWhenNoChanges() {
        // given
        var currentTechSkill = createTechSkill(1);
        var modifiedTechSkill = new TechSkill();

        when(techSkillRepository.findById(1))
                .thenReturn(Optional.of(currentTechSkill));
        when(techSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = techSkillServiceImpl.update(1, modifiedTechSkill);

        // then
        verify(techSkillRepository, times(1)).findById(1);
        verify(techSkillRepository, times(1)).save(any());

        assertEquals(currentTechSkill, result);
    }
}
