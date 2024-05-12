package com.spotit.backend.domain.employee.employeeDetails.course;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.domain.employee.employeeDetails.course.CourseUtils.createCourse;
import static com.spotit.backend.domain.employee.employeeDetails.course.CourseUtils.generateCourseList;
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

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountService;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    CourseServiceImpl courseServiceImpl;

    @Test
    void shouldReturnAllCoursesOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundCourses = generateCourseList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(courseRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundCourses);

        // when
        var result = courseServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(courseRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(foundCourses.size(), result.size());
        assertEquals(foundCourses.get(0), result.get(0));
        assertEquals(foundCourses.get(1), result.get(1));
        assertEquals(foundCourses.get(2), result.get(2));
    }

    @Test
    void shouldReturnCourseById() {
        // given
        var courseId = 1;
        var foundCourse = createCourse(courseId);

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(foundCourse));

        // when
        var result = courseServiceImpl.getById(courseId);

        // then
        verify(courseRepository, times(1)).findById(courseId);

        assertEquals(foundCourse, result);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFound() {
        // given
        var courseId = 1;

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseServiceImpl.getById(courseId));

        // then
        verify(courseRepository, times(1)).findById(courseId);

        assertEquals(getEntityNotFoundMessage(courseId), exception.getMessage());
    }

    @Test
    void shouldReturnCourseWhenCreatedSuccessfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var courseToCreate = createCourse(1);

        when(courseRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = courseServiceImpl.create(userAccountAuth0Id, courseToCreate);

        // then
        verify(courseRepository, times(1)).save(courseToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(courseToCreate.getName(), result.getName());
        assertEquals(courseToCreate.getFinishDate(), result.getFinishDate());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingCourse() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var courseToCreate = createCourse(1);

        when(courseRepository.save(courseToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> courseServiceImpl.create(userAccountAuth0Id, courseToCreate));

        // then
        verify(courseRepository, times(1)).save(courseToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteCourse() {
        // given
        var courseToDeleteId = 1;
        var courseToDelete = createCourse(courseToDeleteId);

        when(courseRepository.findById(courseToDeleteId))
                .thenReturn(Optional.of(courseToDelete));

        // when
        courseServiceImpl.delete(courseToDeleteId);

        // then
        verify(courseRepository, times(1)).findById(courseToDeleteId);
        verify(courseRepository, times(1)).delete(courseToDelete);
    }

    @Test
    void shouldReturnModifiedCourse() {
        // given
        var courseToModifyId = 1;
        var currentCourse = createCourse(courseToModifyId);
        var modifiedCourse = createCourse(2);

        when(courseRepository.findById(courseToModifyId))
                .thenReturn(Optional.of(currentCourse));
        when(courseRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = courseServiceImpl.update(courseToModifyId, modifiedCourse);

        // then
        verify(courseRepository, times(1)).findById(courseToModifyId);
        verify(courseRepository, times(1)).save(any());

        assertEquals(modifiedCourse.getName(), result.getName());
        assertEquals(modifiedCourse.getFinishDate(), result.getFinishDate());
    }

    @Test
    void shouldReturnUnchangedCourseWhenNoChanges() {
        // given
        var courseToModifyId = 1;
        var currentCourse = createCourse(courseToModifyId);
        var modifiedCourse = new Course();

        when(courseRepository.findById(courseToModifyId))
                .thenReturn(Optional.of(currentCourse));
        when(courseRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = courseServiceImpl.update(courseToModifyId, modifiedCourse);

        // then
        verify(courseRepository, times(1)).findById(courseToModifyId);
        verify(courseRepository, times(1)).save(any());

        assertEquals(currentCourse, result);
    }
}
