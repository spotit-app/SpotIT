package com.spotit.backend.domain.referenceData.workExperience;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperience;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperienceWriteDto;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.generateWorkExperienceList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameWriteDto;
import com.spotit.backend.storage.StorageService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class WorkExperienceIntegrationTest extends IntegrationTest {

    static final String WORK_EXPERIENCE_API_URL = "/api/workExperience";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WorkExperienceRepository workExperienceRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StorageService storageService;

    @Test
    void shouldReturnAllWorkExperiences() throws Exception {
        // given
        var createdworkExperiences = generateWorkExperienceList(3);
        workExperienceRepository.saveAll(createdworkExperiences);

        // when
        var result = mockMvc.perform(get(WORK_EXPERIENCE_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdworkExperiences.size()));
    }

    @Test
    void shouldReturnWorkExperienceById() throws Exception {
        // given
        var createdworkExperiences = generateWorkExperienceList(3);
        workExperienceRepository.saveAll(createdworkExperiences);

        // when
        var result = mockMvc
                .perform(get(WORK_EXPERIENCE_API_URL + "/" + createdworkExperiences.get(1).getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdworkExperiences.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenWorkExperienceNotFound() throws Exception {
        // given
        var workExperienceId = 1;

        // when
        var result = mockMvc.perform(get(WORK_EXPERIENCE_API_URL + "/" + workExperienceId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workExperienceId)));
    }

    @Test
    void shouldReturnCreatedWorkExperience() throws Exception {
        // given
        var workExperienceToCreate = createWorkExperienceWriteDto(1);

        // when
        var result = mockMvc.perform(post(WORK_EXPERIENCE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("WorkExperience 1"));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingWorkExperience() throws Exception {
        // given
        var workExperienceToCreate = WorkExperienceWriteDto.builder().build();

        // when
        var result = mockMvc.perform(post(WORK_EXPERIENCE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToCreate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedWorkExperience() throws Exception {
        // given
        var existingWorkExperience = createWorkExperience(1);
        var modifiedWorkExperience = SoftSkillNameWriteDto.builder()
                .name("Name 2")
                .build();
        workExperienceRepository.save(existingWorkExperience);

        // when
        var result = mockMvc.perform(put(WORK_EXPERIENCE_API_URL + "/" + existingWorkExperience.getId())
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedWorkExperience)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name 2"));
    }

    @Test
    void shouldReturnErrorWhenEditedWorkExperienceNotFound() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperienceToModify = createWorkExperienceWriteDto(1);

        // when
        var result = mockMvc.perform(put(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToModify)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workExperienceId)));
    }

    @Test
    void shouldDeleteWorkExperience() throws Exception {
        // given
        var workExperienceToDelete = createWorkExperience(1);
        workExperienceRepository.save(workExperienceToDelete);

        // when
        var result = mockMvc.perform(delete(WORK_EXPERIENCE_API_URL + "/" + workExperienceToDelete.getId())
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(workExperienceToDelete.getId()))));

        assertFalse(workExperienceRepository.existsById(workExperienceToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedWorkExperienceNotFound() throws Exception {
        // given
        var workExperienceId = 1;

        // when
        var result = mockMvc.perform(delete(WORK_EXPERIENCE_API_URL + "/" + workExperienceId).with(
                createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workExperienceId)));
    }
}
