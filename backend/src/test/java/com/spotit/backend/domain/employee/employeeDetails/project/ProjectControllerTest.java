package com.spotit.backend.domain.employee.employeeDetails.project;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employee.employeeDetails.project.ProjectUtils.createProjectReadDto;
import static com.spotit.backend.domain.employee.employeeDetails.project.ProjectUtils.createProjectWriteDto;
import static com.spotit.backend.domain.employee.employeeDetails.project.ProjectUtils.generateProjectsList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(ProjectController.class)
@Import(SecurityConfig.class)
class ProjectControllerTest {

    static final String PROJECT_API_URL = "/api/userAccount/auth0Id1/project";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @MockBean
    ProjectMapper projectMapper;

    @Test
    void shouldReturnListOfUserProjects() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(projectService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateProjectsList(3));
        when(projectMapper.toReadDto(any()))
                .thenReturn(createProjectReadDto(1));

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL).with(createMockJwt("auth0Id1")));

        // then
        verify(projectService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(projectMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedProject() throws Exception {
        // given
        var projectToCreate = createProjectWriteDto(1);
        var createdProject = createProjectReadDto(1);

        when(projectMapper.toReadDto(any()))
                .thenReturn(createdProject);

        // when
        var result = mockMvc.perform(post(PROJECT_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectToCreate)));

        // then
        verify(projectMapper, times(1)).fromWriteDto(any());
        verify(projectService, times(1)).create(any(), any());
        verify(projectMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProject.id()))
                .andExpect(jsonPath("$.description").value(createdProject.description()))
                .andExpect(jsonPath("$.projectUrl").value(createdProject.projectUrl()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateProject() throws Exception {
        // given
        var projectToCreate = createProjectWriteDto(1);
        var creatingProjectError = new ErrorCreatingEntityException();

        when(projectService.create(any(), any()))
                .thenThrow(creatingProjectError);

        // when
        var result = mockMvc.perform(post(PROJECT_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectToCreate)));

        // then
        verify(projectMapper, times(1)).fromWriteDto(projectToCreate);
        verify(projectService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingProjectError.getMessage()));
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        // given
        var projectId = 1;
        var projectToFind = createProjectReadDto(1);

        when(projectMapper.toReadDto(any()))
                .thenReturn(projectToFind);

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL + "/" + projectId).with(createMockJwt("auth0Id1")));

        // then
        verify(projectService, times(1)).getById(projectId);
        verify(projectMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectToFind.id()))
                .andExpect(jsonPath("$.description").value(projectToFind.description()))
                .andExpect(jsonPath("$.projectUrl").value(projectToFind.projectUrl()));
    }

    @Test
    void shouldReturnErrorWhenProjectNotFound() throws Exception {
        // given
        var projectId = 1;
        var projectNotFoundException = new EntityNotFoundException(projectId);

        when(projectService.getById(projectId))
                .thenThrow(projectNotFoundException);

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL + "/" + projectId).with(createMockJwt("auth0Id1")));

        // then
        verify(projectService, times(1)).getById(projectId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(projectNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedProject() throws Exception {
        // given
        var projectId = 1;
        var projectToModify = createProjectWriteDto(1);
        var modifiedProject = createProjectReadDto(1);

        when(projectMapper.toReadDto(any())).thenReturn(modifiedProject);

        // when
        var result = mockMvc.perform(put(PROJECT_API_URL + "/" + projectId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectToModify)));

        // then
        verify(projectMapper, times(1)).fromWriteDto(any());
        verify(projectService, times(1)).update(any(), any());
        verify(projectMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedProject.id()))
                .andExpect(jsonPath("$.description").value(modifiedProject.description()))
                .andExpect(jsonPath("$.projectUrl").value(modifiedProject.projectUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedProjectNotFound() throws Exception {
        // given
        var projectId = 1;
        var projectToModify = createProjectWriteDto(1);
        var projectNotFoundException = new EntityNotFoundException(projectId);

        when(projectService.update(any(), any()))
                .thenThrow(projectNotFoundException);

        // when
        var result = mockMvc.perform(put(PROJECT_API_URL + "/" + projectId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectToModify)));

        // then
        verify(projectMapper, times(1)).fromWriteDto(any());
        verify(projectService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(projectNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        // given
        var projectId = 1;

        // when
        var result = mockMvc.perform(delete(PROJECT_API_URL + "/" + projectId).with(createMockJwt("auth0Id1")));

        // then
        verify(projectService, times(1)).delete(projectId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(projectId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedProjectNotFound() throws Exception {
        // given
        var projectId = 1;
        var projectNotFoundException = new EntityNotFoundException(projectId);

        doThrow(projectNotFoundException).when(projectService).delete(projectId);

        // when
        var result = mockMvc.perform(delete(PROJECT_API_URL + "/" + projectId).with(createMockJwt("auth0Id1")));

        // then
        verify(projectService, times(1)).delete(projectId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(projectNotFoundException.getMessage()));
    }
}
