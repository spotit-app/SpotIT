package com.spotit.backend.employee.userDetails.education;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userDetails.education.EducationUtils.createEducation;
import static com.spotit.backend.employee.userDetails.education.EducationUtils.generateEducationList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
public class EducationServiceTest {

    @Mock
    EducationRepository educationRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    EducationServiceImpl educationServiceImpl;

    @Test
    void shouldReturnEveryEducation() {
        // given
        var auth0Id = "auth0Id1";
        var foundEducation = generateEducationList(3);

        when(userAccountService.getByAuth0Id(auth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(auth0Id);
        when(educationRepository.findByUserAccountAuth0Id(auth0Id))
                .thenReturn(foundEducation);

        // when
        var result = educationServiceImpl.getAllByUserAccountAuth0Id(auth0Id);

        // then
        verify(educationRepository, times(1)).findByUserAccountAuth0Id(auth0Id);

        assertEquals(foundEducation.size(), result.size());
        assertEquals(foundEducation.get(0), result.get(0));
        assertEquals(foundEducation.get(1), result.get(1));
        assertEquals(foundEducation.get(2), result.get(2));
    }

    @Test
    void shouldReturnEducationById() {
        // given
        var foundEducation = createEducation(1);

        when(educationRepository.findById(1)).thenReturn(Optional.of(foundEducation));

        // when
        var result = educationServiceImpl.getById(1);

        // then
        verify(educationRepository, times(1)).findById(1);

        assertEquals(foundEducation, result);
    }

    @Test
    void shouldThrowExceptionWhenEducationNotFound() {
        // given
        when(educationRepository.findById(1))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> educationServiceImpl.getById(1));

        // then
        verify(educationRepository, times(1)).findById(1);

        assertEquals(getEntityNotFoundMessage(1), exception.getMessage());
    }

    @Test
    void shouldReturnEducationWhenCreatedSuccessfully() {
        // given
        var auth0Id = "auth0Id1";
        var educationToCreate = createEducation(1);

        when(educationRepository.save(educationToCreate))
                .thenReturn(educationToCreate);

        // when
        var result = educationServiceImpl.create(auth0Id, educationToCreate);

        // then
        verify(educationRepository, times(1)).save(educationToCreate);

        assertEquals(educationToCreate, result);
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingEducation() {
        // given
        var auth0Id = "auth0Id1";
        var educationToCreate = createEducation(1);

        when(educationRepository.save(educationToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> educationServiceImpl.create(auth0Id, educationToCreate));

        // then
        verify(educationRepository, times(1)).save(educationToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteEducationById() {
        // given
        var educationToDelete = createEducation(1);

        when(educationRepository.findById(1)).thenReturn(Optional.of(educationToDelete));

        // when
        educationServiceImpl.delete(1);

        // then
        verify(educationRepository, times(1)).findById(1);
        verify(educationRepository, times(1)).delete(educationToDelete);
    }

    @Test
    void shouldReturnModifiedEducation() {
        // given
        var currentEducation = createEducation(1);
        var modifiedEducation = createEducation(2);
        modifiedEducation.setId(currentEducation.getId());
        modifiedEducation.setEducationLevel(currentEducation.getEducationLevel());

        when(educationRepository.findById(1))
                .thenReturn(Optional.of(currentEducation));
        when(educationRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationServiceImpl.update(currentEducation.getId(), modifiedEducation);

        // then
        verify(educationRepository, times(1)).findById(1);
        verify(educationRepository, times(1)).save(modifiedEducation);

        assertEquals(modifiedEducation, result);
    }

    @Test
    void shouldReturnPartiallymodifiedEducation() {
        // given
        var currentEducation = createEducation(1);
        var modifiedEducation = Education.builder()
                .id(currentEducation.getId())
                .educationLevel(currentEducation.getEducationLevel())
                .schoolName("schoolName")
                .faculty("faculty")
                .startDate(LocalDate.parse("2023-12-22"))
                .endDate(LocalDate.parse("2023-12-23"))
                .build();

        when(educationRepository.findById(1))
                .thenReturn(Optional.of(currentEducation));
        when(educationRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationServiceImpl.update(currentEducation.getId(), modifiedEducation);

        // then
        verify(educationRepository, times(1)).findById(1);
        verify(educationRepository, times(1)).save(modifiedEducation);

        assertEquals(modifiedEducation, result);
    }

    @Test
    void shouldReturnUnchangedEducationWhenNoChanges() {
        // given
        var currentEducation = createEducation(1);
        var modifiedEducation = new Education();

        when(educationRepository.findById(1))
                .thenReturn(Optional.of(currentEducation));
        when(educationRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = educationServiceImpl.update(currentEducation.getId(), modifiedEducation);

        // then
        verify(educationRepository, times(1)).findById(1);
        verify(educationRepository, times(1)).save(any());

        assertEquals(currentEducation, result);
    }
}
