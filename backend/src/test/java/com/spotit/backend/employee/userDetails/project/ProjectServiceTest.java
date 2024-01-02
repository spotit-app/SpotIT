package com.spotit.backend.employee.userDetails.project;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.project.ProjectUtils.createProject;
import static com.spotit.backend.employee.userDetails.project.ProjectUtils.generateProjectsList;
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
class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @InjectMocks
    ProjectServiceImpl projectServiceImpl;

    @Test
    void shouldReturnAllProjectsOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundProjects = generateProjectsList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(projectRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundProjects);

        // when
        var result = projectServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(projectRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(3, result.size());
        assertEquals(foundProjects.get(0), result.get(0));
        assertEquals(foundProjects.get(1), result.get(1));
        assertEquals(foundProjects.get(2), result.get(2));
    }

    @Test
    void shouldReturnProjectById() {
        // given
        var projectId = 1;
        var foundProject = createProject(projectId);

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(foundProject));

        // when
        var result = projectServiceImpl.getById(projectId);

        // then
        verify(projectRepository, times(1)).findById(projectId);

        assertEquals(foundProject, result);
    }

    @Test
    void shouldThrowExceptionWhenProjectNotFound() {
        // given
        var projectId = 1;

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> projectServiceImpl.getById(projectId));

        // then
        verify(projectRepository, times(1)).findById(projectId);

        assertEquals(getEntityNotFoundMessage(projectId), exception.getMessage());
    }

    @Test
    void shouldReturnProjectWhenCreatedSuccesfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var projectToCreate = createProject(1);

        when(projectRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = projectServiceImpl.create(userAccountAuth0Id, projectToCreate);

        // then
        verify(projectRepository, times(1)).save(projectToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(projectToCreate.getDescription(), result.getDescription());
        assertEquals(projectToCreate.getProjectUrl(), result.getProjectUrl());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingProject() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var projectToCreate = createProject(1);

        when(projectRepository.save(projectToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> projectServiceImpl.create(userAccountAuth0Id, projectToCreate));

        // then
        verify(projectRepository, times(1)).save(projectToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteProject() {
        // given
        var projectToDeleteId = 1;
        var projectToDelete = createProject(projectToDeleteId);

        when(projectRepository.findById(projectToDeleteId))
                .thenReturn(Optional.of(projectToDelete));

        // when
        projectServiceImpl.delete(projectToDeleteId);

        // then
        verify(projectRepository, times(1)).findById(projectToDeleteId);
        verify(projectRepository, times(1)).delete(projectToDelete);
    }

    @Test
    void shouldReturnModifiedProject() {
        // given
        var projectToModifyId = 1;
        var currentProject = createProject(projectToModifyId);
        var modifiedProject = createProject(2);

        when(projectRepository.findById(projectToModifyId))
                .thenReturn(Optional.of(currentProject));
        when(projectRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = projectServiceImpl.update(projectToModifyId, modifiedProject);

        // then
        verify(projectRepository, times(1)).findById(projectToModifyId);
        verify(projectRepository, times(1)).save(any());

        assertEquals(modifiedProject.getDescription(), result.getDescription());
        assertEquals(modifiedProject.getProjectUrl(), result.getProjectUrl());
    }

    @Test
    void shouldReturnUnchangedProjectWhenNoChanges() {
        // given
        var projectToModifyId = 1;
        var currentProject = createProject(projectToModifyId);
        var modifiedProject = new Project();

        when(projectRepository.findById(projectToModifyId))
                .thenReturn(Optional.of(currentProject));
        when(projectRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = projectServiceImpl.update(projectToModifyId, modifiedProject);

        // then
        verify(projectRepository, times(1)).findById(projectToModifyId);
        verify(projectRepository, times(1)).save(any());

        assertEquals(currentProject, result);
    }
}
