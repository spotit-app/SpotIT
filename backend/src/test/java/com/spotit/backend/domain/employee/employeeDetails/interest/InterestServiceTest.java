package com.spotit.backend.domain.employee.employeeDetails.interest;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employee.employeeDetails.interest.InterestUtils.createInterest;
import static com.spotit.backend.domain.employee.employeeDetails.interest.InterestUtils.generateInterestList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
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
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountService;

@ExtendWith(MockitoExtension.class)
class InterestServiceTest {

    @Mock
    InterestRepository interestRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    InterestServiceImpl interestServiceImpl;

    @Test
    void shouldReturnAllInterestsOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundInterests = generateInterestList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(interestRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundInterests);

        // when
        var result = interestServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(interestRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(3, result.size());
        assertEquals(foundInterests.get(0), result.get(0));
        assertEquals(foundInterests.get(1), result.get(1));
        assertEquals(foundInterests.get(2), result.get(2));
    }

    @Test
    void shouldReturnInterestById() {
        // given
        var interestId = 1;
        var foundInterest = createInterest(interestId);

        when(interestRepository.findById(interestId))
                .thenReturn(Optional.of(foundInterest));

        // when
        var result = interestServiceImpl.getById(interestId);

        // then
        verify(interestRepository, times(1)).findById(interestId);

        assertEquals(foundInterest, result);
    }

    @Test
    void shouldThrowExceptionWhenInterestNotFound() {
        // given
        var interestId = 1;

        when(interestRepository.findById(interestId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> interestServiceImpl.getById(interestId));

        // then
        verify(interestRepository, times(1)).findById(interestId);

        assertEquals(getEntityNotFoundMessage(interestId), exception.getMessage());
    }

    @Test
    void shouldReturnInterestWhenCreatedSuccessfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var interestToCreate = createInterest(1);

        when(interestRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = interestServiceImpl.create(userAccountAuth0Id, interestToCreate);

        // then
        verify(interestRepository, times(1)).save(interestToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(interestToCreate.getName(), result.getName());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingInterest() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var interestToCreate = createInterest(1);

        when(interestRepository.save(interestToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> interestServiceImpl.create(userAccountAuth0Id, interestToCreate));

        // then
        verify(interestRepository, times(1)).save(interestToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteInterest() {
        // given
        var interestToDeleteId = 1;
        var interestToDelete = createInterest(interestToDeleteId);

        when(interestRepository.findById(interestToDeleteId))
                .thenReturn(Optional.of(interestToDelete));

        // when
        interestServiceImpl.delete(interestToDeleteId);

        // then
        verify(interestRepository, times(1)).findById(interestToDeleteId);
        verify(interestRepository, times(1)).delete(interestToDelete);
    }

    @Test
    void shouldReturnModifiedInterest() {
        // given
        var interestToModifyId = 1;
        var currentInterest = createInterest(interestToModifyId);
        var modifiedInterest = createInterest(2);

        when(interestRepository.findById(interestToModifyId))
                .thenReturn(Optional.of(currentInterest));
        when(interestRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = interestServiceImpl.update(interestToModifyId, modifiedInterest);

        // then
        verify(interestRepository, times(1)).findById(interestToModifyId);
        verify(interestRepository, times(1)).save(any());

        assertEquals(modifiedInterest.getName(), result.getName());
    }

    @Test
    void shouldReturnUnchangedInterestWhenNoChanges() {
        // given
        var interestToModifyId = 1;
        var currentInterest = createInterest(interestToModifyId);
        var modifiedInterest = new Interest();

        when(interestRepository.findById(interestToModifyId))
                .thenReturn(Optional.of(currentInterest));
        when(interestRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = interestServiceImpl.update(interestToModifyId, modifiedInterest);

        // then
        verify(interestRepository, times(1)).findById(interestToModifyId);
        verify(interestRepository, times(1)).save(any());

        assertEquals(currentInterest, result);
    }
}
