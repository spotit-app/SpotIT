package com.spotit.backend.employee.userDetails.experience;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.experience.ExperienceUtils.createExperience;
import static com.spotit.backend.employee.userDetails.experience.ExperienceUtils.generateExperienceList;
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
class ExperienceServiceTest {

    @Mock
    ExperienceRepository experienceRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    ExperienceServiceImpl experienceServiceImpl;

    @Test
    void shouldReturnAllExperiencesOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundExperiences = generateExperienceList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(experienceRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundExperiences);

        // when
        var result = experienceServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(experienceRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(foundExperiences.size(), result.size());
        assertEquals(foundExperiences.get(0), result.get(0));
        assertEquals(foundExperiences.get(1), result.get(1));
        assertEquals(foundExperiences.get(2), result.get(2));
    }

    @Test
    void shouldReturnExperienceById() {
        // given
        var experienceId = 1;
        var foundExperience = createExperience(experienceId);

        when(experienceRepository.findById(experienceId))
                .thenReturn(Optional.of(foundExperience));

        // when
        var result = experienceServiceImpl.getById(experienceId);

        // then
        verify(experienceRepository, times(1)).findById(experienceId);

        assertEquals(foundExperience, result);
    }

    @Test
    void shouldThrowExceptionWhenExperienceNotFound() {
        // given
        var experienceId = 1;

        when(experienceRepository.findById(experienceId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> experienceServiceImpl.getById(experienceId));

        // then
        verify(experienceRepository, times(1)).findById(experienceId);

        assertEquals(getEntityNotFoundMessage(experienceId), exception.getMessage());
    }

    @Test
    void shouldReturnExperienceWhenCreatedSuccesfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var experienceToCreate = createExperience(1);

        when(experienceRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = experienceServiceImpl.create(userAccountAuth0Id, experienceToCreate);

        // then
        verify(experienceRepository, times(1)).save(experienceToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(experienceToCreate.getCompanyName(), result.getCompanyName());
        assertEquals(experienceToCreate.getPosition(), result.getPosition());
        assertEquals(experienceToCreate.getStartDate(), result.getStartDate());
        assertEquals(experienceToCreate.getEndDate(), result.getEndDate());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingExperience() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var experienceToCreate = createExperience(1);

        when(experienceRepository.save(experienceToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> experienceServiceImpl.create(userAccountAuth0Id, experienceToCreate));

        // then
        verify(experienceRepository, times(1)).save(experienceToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteExperience() {
        // given
        var experienceToId = 1;
        var experienceToDelte = createExperience(experienceToId);

        when(experienceRepository.findById(experienceToId))
                .thenReturn(Optional.of(experienceToDelte));

        // when
        experienceServiceImpl.delete(experienceToId);

        // then
        verify(experienceRepository, times(1)).findById(experienceToId);
        verify(experienceRepository, times(1)).delete(experienceToDelte);
    }

    @Test
    void shouldReturnModifiedExperience() {
        // given
        var experienceToModifyId = 1;
        var currentExperience = createExperience(experienceToModifyId);
        var modifiedExperience = createExperience(2);

        when(experienceRepository.findById(experienceToModifyId))
                .thenReturn(Optional.of(currentExperience));
        when(experienceRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = experienceServiceImpl.update(experienceToModifyId, modifiedExperience);

        // then
        verify(experienceRepository, times(1)).findById(experienceToModifyId);
        verify(experienceRepository, times(1)).save(any());

        assertEquals(modifiedExperience.getCompanyName(), result.getCompanyName());
        assertEquals(modifiedExperience.getPosition(), result.getPosition());
        assertEquals(modifiedExperience.getStartDate(), result.getStartDate());
        assertEquals(modifiedExperience.getEndDate(), result.getEndDate());

    }

    @Test
    void shouldReturnUnchangedExperienceWhenNoChanges() {
        // given
        var experienceToModifyId = 1;
        var currentExperience = createExperience(experienceToModifyId);
        var modifiedExperience = new Experience();

        when(experienceRepository.findById(experienceToModifyId))
                .thenReturn(Optional.of(currentExperience));
        when(experienceRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = experienceServiceImpl.update(experienceToModifyId, modifiedExperience);

        // then
        verify(experienceRepository, times(1)).findById(experienceToModifyId);
        verify(experienceRepository, times(1)).save(any());

        assertEquals(currentExperience, result);
    }
}
