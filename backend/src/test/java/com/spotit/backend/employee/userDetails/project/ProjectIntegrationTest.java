package com.spotit.backend.employee.userDetails.project;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.project.ProjectUtils.createProject;
import static com.spotit.backend.employee.userDetails.project.ProjectUtils.createProjectWriteDto;
import static com.spotit.backend.employee.userDetails.project.ProjectUtils.generateProjectsList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProjectIntegrationTest extends IntegrationTest {

    static final String PROJECT_API_URL = "/api/userAccount/auth0Id1/project";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ProjectRepository projectRepository;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserProjects() throws Exception {
        // given
        var projectsToCreate = generateProjectsList(3);
        projectsToCreate.stream().forEach(project -> project.setUserAccount(userAccount));
        projectRepository.saveAll(projectsToCreate);

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        // given
        var projectsToCreate = generateProjectsList(3);
        projectsToCreate.stream().forEach(project -> project.setUserAccount(userAccount));
        var createdProjects = projectRepository.saveAll(projectsToCreate);
        var wantedProject = createdProjects.get(1);

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL + "/" + wantedProject.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedProject.getId()))
                .andExpect(jsonPath("$.description").value(wantedProject.getDescription()))
                .andExpect(jsonPath("$.projectUrl").value(wantedProject.getProjectUrl()));
    }

    @Test
    void shouldReturnErrorWhenProjectNotFound() throws Exception {
        // given
        var projectId = 1;

        // when
        var result = mockMvc.perform(get(PROJECT_API_URL + "/" + projectId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(projectId)));
    }

    @Test
    void shouldReturnCreatedProject() throws Exception {
        // given
        var projectToCreate = createProjectWriteDto(1);

        // when
        var result = mockMvc.perform(post(PROJECT_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(projectToCreate.description()))
                .andExpect(jsonPath("$.projectUrl").value(projectToCreate.projectUrl()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingProject() throws Exception {
        // given
        var projectWithTooLongUrl = ProjectWriteDto.builder()
                .description("Description")
                .projectUrl("url".repeat(100))
                .build();

        // when
        var result = mockMvc.perform(post(PROJECT_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectWithTooLongUrl)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedProject() throws Exception {
        // given
        var projectToCreate = createProject(1);
        projectToCreate.setUserAccount(userAccount);
        var existingProject = projectRepository.save(projectToCreate);
        var modifiedProject = createProjectWriteDto(2);

        // when
        var result = mockMvc.perform(put(PROJECT_API_URL + "/" + existingProject.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedProject)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProject.getId()))
                .andExpect(jsonPath("$.description").value(modifiedProject.description()))
                .andExpect(jsonPath("$.projectUrl").value(modifiedProject.projectUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedProjectNotFound() throws Exception {
        // given
        var projectId = 1;
        var modifiedProject = createProjectWriteDto(2);

        // when
        var result = mockMvc.perform(put(PROJECT_API_URL + "/" + projectId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedProject)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(projectId)));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        // given
        var projectToCreate = createProject(1);
        projectToCreate.setUserAccount(userAccount);
        var projectToDelete = projectRepository.save(projectToCreate);

        // when
        var result = mockMvc.perform(delete(PROJECT_API_URL + "/" + projectToDelete.getId()).with(jwt()));

        // then
        assertFalse(projectRepository.existsById(projectToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(projectToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedProjectNotFound() throws Exception {
        // given
        var projectId = 1;

        // when
        var result = mockMvc.perform(delete(PROJECT_API_URL + "/" + projectId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(projectId)));
    }
}
