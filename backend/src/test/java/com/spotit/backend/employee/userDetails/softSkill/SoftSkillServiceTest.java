package com.spotit.backend.employee.userDetails.softSkill;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userDetails.softSkill.SoftSkillUtils.createSoftSkill;
import static com.spotit.backend.employee.userDetails.softSkill.SoftSkillUtils.generateSoftSkillList;
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

import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountService;

@ExtendWith(MockitoExtension.class)
public class SoftSkillServiceTest {

    @Mock
    SoftSkillRepository softSkillRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    SoftSkillServiceImpl softSkillServiceImpl;

    @Test
    void shouldReturnAllSoftSkills() {
        // given
        var auth0Id = "auth0Id1";
        var foundSoftSkills = generateSoftSkillList(3);

        when(userAccountService.getByAuth0Id(auth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(auth0Id);
        when(softSkillRepository.findByUserAccountAuth0Id(auth0Id))
                .thenReturn(foundSoftSkills);

        // when
        var result = softSkillServiceImpl.getAllByUserAccountAuth0Id(auth0Id);

        // then
        verify(softSkillRepository, times(1)).findByUserAccountAuth0Id(auth0Id);

        assertEquals(foundSoftSkills.size(), result.size());
        assertEquals(foundSoftSkills.get(0), result.get(0));
        assertEquals(foundSoftSkills.get(1), result.get(1));
        assertEquals(foundSoftSkills.get(2), result.get(2));
    }

    @Test
    void shouldReturnSoftSkillById() {
        // given
        var foundSoftSkill = createSoftSkill(1);

        when(softSkillRepository.findById(1)).thenReturn(Optional.of(foundSoftSkill));

        // when
        var result = softSkillServiceImpl.getById(1);

        // then
        verify(softSkillRepository, times(1)).findById(1);

        assertEquals(foundSoftSkill, result);
    }

    @Test
    void shouldThrowExceptionWhenSoftSkillNotFound() {
        // given
        when(softSkillRepository.findById(1))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> softSkillServiceImpl.getById(1));

        // then
        verify(softSkillRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnSoftSkillWhenCreatedSuccessfully() {
        // given
        var auth0Id = "auth0Id1";
        var softSkillToCreate = createSoftSkill(1);

        when(softSkillRepository.save(softSkillToCreate))
                .thenReturn(softSkillToCreate);

        // when
        var result = softSkillServiceImpl.create(auth0Id, softSkillToCreate);

        // then
        verify(softSkillRepository, times(1)).save(softSkillToCreate);

        assertEquals(softSkillToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingSoftSkill() {
        // given
        var auth0Id = "auth0Id1";
        var softSkillToCreate = createSoftSkill(1);

        when(softSkillRepository.save(softSkillToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> softSkillServiceImpl.create(auth0Id, softSkillToCreate));

        // then
        verify(softSkillRepository, times(1)).save(softSkillToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteSoftSkillById() {
        // given
        var softSkillToDelete = createSoftSkill(1);

        when(softSkillRepository.findById(1)).thenReturn(Optional.of(softSkillToDelete));

        // when
        softSkillServiceImpl.delete(1);

        // then
        verify(softSkillRepository, times(1)).findById(1);
        verify(softSkillRepository, times(1)).delete(softSkillToDelete);
    }

    @Test
    void shouldReturnModifiedSoftSkill() {
        // given
        var currentSoftSkill = createSoftSkill(1);
        var modifiedSoftSkill = createSoftSkill(2);
        modifiedSoftSkill.setId(currentSoftSkill.getId());
        modifiedSoftSkill.setSoftSkillName(currentSoftSkill.getSoftSkillName());

        when(softSkillRepository.findById(1))
                .thenReturn(Optional.of(currentSoftSkill));
        when(softSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillServiceImpl.update(currentSoftSkill.getId(), modifiedSoftSkill);

        // then
        verify(softSkillRepository, times(1)).findById(1);
        verify(softSkillRepository, times(1)).save(modifiedSoftSkill);

        assertEquals(modifiedSoftSkill, result);
    }

    @Test
    void shouldReturnPartiallyModifiedSoftSkill() {
        // given
        var currentSoftSkill = createSoftSkill(1);
        var modifiedSoftSkill = SoftSkill.builder()
                .id(currentSoftSkill.getId())
                .softSkillName(currentSoftSkill.getSoftSkillName())
                .skillLevel(5)
                .build();

        when(softSkillRepository.findById(1))
                .thenReturn(Optional.of(currentSoftSkill));
        when(softSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillServiceImpl.update(currentSoftSkill.getId(), modifiedSoftSkill);

        // then
        verify(softSkillRepository, times(1)).findById(1);
        verify(softSkillRepository, times(1)).save(modifiedSoftSkill);

        assertEquals(modifiedSoftSkill, result);
    }

    @Test
    void shouldReturnUnchangedSoftSkillWhenNoChanges() {
        // given
        var currentSoftSkill = createSoftSkill(1);
        var modifiedSoftSkill = new SoftSkill();

        when(softSkillRepository.findById(1))
                .thenReturn(Optional.of(currentSoftSkill));
        when(softSkillRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = softSkillServiceImpl.update(currentSoftSkill.getId(), modifiedSoftSkill);

        // then
        verify(softSkillRepository, times(1)).findById(1);
        verify(softSkillRepository, times(1)).save(any());

        assertEquals(currentSoftSkill, result);
    }
}
