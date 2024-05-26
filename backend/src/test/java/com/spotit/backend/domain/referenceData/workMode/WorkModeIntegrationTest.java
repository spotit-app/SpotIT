package com.spotit.backend.domain.referenceData.workMode;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.createWorkMode;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.createWorkModeWriteDto;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.generateWorkModeList;
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
class WorkModeIntegrationTest extends IntegrationTest {

    static final String WORK_MODE_API_URL = "/api/workMode";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WorkModeRepository workModeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StorageService storageService;

    @Test
    void shouldReturnAllworkModes() throws Exception {
        // given
        var createdWorkModes = generateWorkModeList(3);
        workModeRepository.saveAll(createdWorkModes);

        // when
        var result = mockMvc.perform(get(WORK_MODE_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdWorkModes.size()));
    }

    @Test
    void shouldReturnworkModeById() throws Exception {
        // given
        var createdWorkModes = generateWorkModeList(3);
        workModeRepository.saveAll(createdWorkModes);

        // when
        var result = mockMvc
                .perform(get(WORK_MODE_API_URL + "/" + createdWorkModes.get(1).getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdWorkModes.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenWorkModeNotFound() throws Exception {
        // given
        var workModeId = 1;

        // when
        var result = mockMvc.perform(get(WORK_MODE_API_URL + "/" + workModeId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workModeId)));
    }

    @Test
    void shouldReturnCreatedWorkMode() throws Exception {
        // given
        var workModeToCreate = createWorkModeWriteDto(1);

        // when
        var result = mockMvc.perform(post(WORK_MODE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("WorkMode 1"));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingWorkMode() throws Exception {
        // given
        var workModeToCreate = WorkModeWriteDto.builder().build();

        // when
        var result = mockMvc.perform(post(WORK_MODE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToCreate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedWorkMode() throws Exception {
        // given
        var existingWorkMode = createWorkMode(1);
        var modifiedWorkMode = SoftSkillNameWriteDto.builder()
                .name("Name 2")
                .build();
        workModeRepository.save(existingWorkMode);

        // when
        var result = mockMvc.perform(put(WORK_MODE_API_URL + "/" + existingWorkMode.getId())
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedWorkMode)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name 2"));
    }

    @Test
    void shouldReturnErrorWhenEditedWorkModeNotFound() throws Exception {
        // given
        var workModeId = 1;
        var workModeToModify = createWorkModeWriteDto(1);

        // when
        var result = mockMvc.perform(put(WORK_MODE_API_URL + "/" + workModeId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToModify)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workModeId)));
    }

    @Test
    void shouldDeleteWorkMode() throws Exception {
        // given
        var workModeToDelete = createWorkMode(1);
        workModeRepository.save(workModeToDelete);

        // when
        var result = mockMvc.perform(delete(WORK_MODE_API_URL + "/" + workModeToDelete.getId())
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(workModeToDelete.getId()))));

        assertFalse(workModeRepository.existsById(workModeToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedWorkModeNotFound() throws Exception {
        // given
        var workModeId = 1;

        // when
        var result = mockMvc.perform(delete(WORK_MODE_API_URL + "/" + workModeId).with(
                createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(workModeId)));
    }
}
